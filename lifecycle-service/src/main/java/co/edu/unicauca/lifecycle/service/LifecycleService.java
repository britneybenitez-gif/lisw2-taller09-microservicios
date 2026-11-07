package co.edu.unicauca.lifecycle.service;
import co.edu.unicauca.lifecycle.dto.*;
import co.edu.unicauca.lifecycle.model.*;
import co.edu.unicauca.lifecycle.repository.*;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
@Service public class LifecycleService {
 private final QuestionStateRepository states;
 private final OperationResultRepository operations;
 private static final Map<QuestionStatus,Set<QuestionStatus>> ALLOWED=Map.of(
  QuestionStatus.DRAFT,Set.of(QuestionStatus.PENDING_REVIEW),
  QuestionStatus.PENDING_REVIEW,Set.of(QuestionStatus.IN_REVIEW),
  QuestionStatus.IN_REVIEW,Set.of(QuestionStatus.APPROVED,QuestionStatus.REJECTED),
  QuestionStatus.REJECTED,Set.of(QuestionStatus.DRAFT),
  QuestionStatus.APPROVED,Set.of(QuestionStatus.PUBLISHED),
  QuestionStatus.PUBLISHED,Set.of());
 public LifecycleService(QuestionStateRepository states,OperationResultRepository operations) {this.states=states;this.operations=operations;}
 @Transactional public QuestionState initialize(Long id) {
  if(id==null || id<=0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"ID inválido");
  return states.findById(id).orElseGet(()->states.save(new QuestionState(id)));
 }
 public QuestionState get(Long id) {
  return states.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Estado de pregunta no encontrado"));
 }
 public OperationResult getOperation(String id) {
  return operations.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Operación todavía no procesada"));
 }
 @Transactional public QuestionState transition(Long id,TransitionRequest req) {
  QuestionState state=get(id);
  if(req==null || req.expectedStatus()==null || req.targetStatus()==null)
   throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Indique expectedStatus y targetStatus");
  if(state.getStatus()!=req.expectedStatus())
   throw new ResponseStatusException(HttpStatus.CONFLICT,"Estado actual "+state.getStatus()+"; se esperaba "+req.expectedStatus());
  if(!ALLOWED.getOrDefault(state.getStatus(),Set.of()).contains(req.targetStatus()))
   throw new ResponseStatusException(HttpStatus.CONFLICT,"Transición no permitida: "+state.getStatus()+" -> "+req.targetStatus());
  if(req.targetStatus()==QuestionStatus.REJECTED && (req.reason()==null || req.reason().isBlank()))
   throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"El rechazo requiere motivo");
  state.transition(req.targetStatus(),req.reason());
  return states.save(state);
 }
 @Transactional public void processSubmit(SubmitQuestionCommand command) {
  if(command==null || command.operationId()==null || command.operationId().isBlank()) return;
  if(operations.existsById(command.operationId())) return; // Mensaje duplicado: procesamiento idempotente.
  String outcome="COMPLETED", detail="Transición DRAFT -> PENDING_REVIEW";
  try {
   transition(command.questionId(),new TransitionRequest(QuestionStatus.DRAFT,QuestionStatus.PENDING_REVIEW,"Solicitud de revisión "+command.operationId()));
  } catch(Exception ex) {
   outcome="FAILED"; detail=ex.getMessage()==null ? "No fue posible procesar la transición" : ex.getMessage();
  }
  operations.save(new OperationResult(command.operationId(),command.questionId(),outcome,detail));
 }
}
