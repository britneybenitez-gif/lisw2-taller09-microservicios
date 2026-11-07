package co.edu.unicauca.review.service;
import co.edu.unicauca.review.client.*;
import co.edu.unicauca.review.dto.*;
import co.edu.unicauca.review.messaging.EventPublisher;
import co.edu.unicauca.review.model.ReviewAssignment;
import co.edu.unicauca.review.repository.ReviewAssignmentRepository;
import java.util.*;
import org.springframework.amqp.AmqpException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
@Service public class ReviewService {
 private final ReviewAssignmentRepository assignments;
 private final UserClient users;
 private final LifecycleClient lifecycle;
 private final EventPublisher publisher;
 public ReviewService(ReviewAssignmentRepository assignments,UserClient users,LifecycleClient lifecycle,EventPublisher publisher){
  this.assignments=assignments;this.users=users;this.lifecycle=lifecycle;this.publisher=publisher;
 }
 public ReviewAssignment assign(String auth,AssignRequest request){
  users.requireAdmin(auth);
  if(request==null || request.questionId()==null || request.reviewerId()==null)
   throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"questionId y reviewerId obligatorios");
  users.ensureEligibleReviewer(request.reviewerId());
  if(!"PENDING_REVIEW".equals(lifecycle.get(request.questionId()).status()))
   throw new ResponseStatusException(HttpStatus.CONFLICT,"La pregunta no está PENDING_REVIEW");
  if(assignments.findFirstByQuestionIdOrderByIdDesc(request.questionId()).filter(a->!"DECIDED".equals(a.getStatus())).isPresent())
   throw new ResponseStatusException(HttpStatus.CONFLICT,"Esta pregunta ya tiene asignación activa o pendiente");
  ReviewAssignment a=assignments.save(new ReviewAssignment(request.questionId(),request.reviewerId()));
  try{
   lifecycle.transition(request.questionId(),"PENDING_REVIEW","IN_REVIEW","Asignación revisor "+request.reviewerId());
  }catch(ResponseStatusException ex){
   // Ante 409 seguro: transición no ocurrió; ante 503 incierto: guardar RESERVATION para reconciliar.
   if(ex.getStatusCode().is4xxClientError()) assignments.deleteById(a.getId());
   throw ex;
  }
  a.status("NOTIFICATION_PENDING");assignments.save(a);
  return publishOrKeepPending(a);
 }
 private ReviewAssignment publishOrKeepPending(ReviewAssignment a){
  try{
   publisher.assigned(a);
   a.status("ACTIVE");
  }catch(AmqpException e){a.status("NOTIFICATION_PENDING");}
  return assignments.save(a);
 }
 public ReviewAssignment reconcile(String auth,Long id){
  users.requireAdmin(auth);
  ReviewAssignment a=find(id);
  if(!"RESERVED".equals(a.getStatus()))
   throw new ResponseStatusException(HttpStatus.CONFLICT,"Solo se reconcilian reservas pendientes");
  String status=lifecycle.get(a.getQuestionId()).status();
  if("PENDING_REVIEW".equals(status)){
   assignments.deleteById(id);
   throw new ResponseStatusException(HttpStatus.CONFLICT,"No hubo transición. Reserva eliminada; reintente asignar");
  }
  if(!"IN_REVIEW".equals(status))
   throw new ResponseStatusException(HttpStatus.CONFLICT,"Estado inesperado "+status+"; requiere revisión manual");
  a.status("NOTIFICATION_PENDING");assignments.save(a);
  return publishOrKeepPending(a);
 }
 public ReviewAssignment retryNotification(String auth,Long id){
  users.requireAdmin(auth);
  ReviewAssignment a=find(id);
  if(!"NOTIFICATION_PENDING".equals(a.getStatus()))
   throw new ResponseStatusException(HttpStatus.CONFLICT,"No hay notificación pendiente");
  return publishOrKeepPending(a);
 }
 public ReviewAssignment decide(String auth,Long id,ReviewDecision request){
  UserProfile reviewer=users.requireReviewer(auth);
  ReviewAssignment a=find(id);
  if(!Objects.equals(a.getReviewerId(),reviewer.id())) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Revisor diferente al asignado");
  if(!"ACTIVE".equals(a.getStatus()) && !"NOTIFICATION_PENDING".equals(a.getStatus()))
   throw new ResponseStatusException(HttpStatus.CONFLICT,"Asignación no activa");
  if(request==null || !("APPROVED".equals(request.decision()) || "REJECTED".equals(request.decision())))
   throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Decisión permitida: APPROVED o REJECTED");
  if("REJECTED".equals(request.decision()) && (request.reason()==null || request.reason().isBlank()))
   throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Indique el motivo del rechazo");
  lifecycle.transition(a.getQuestionId(),"IN_REVIEW",request.decision(),request.reason());
  a.decide(request.decision(),request.reason());
  return assignments.save(a);
 }
 public ReviewAssignment find(Long id){return assignments.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Asignación no encontrada"));}
 public List<ReviewAssignment> list(){return assignments.findAll();}
}
