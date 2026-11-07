package co.edu.unicauca.question.service;
import co.edu.unicauca.question.client.*;
import co.edu.unicauca.question.dto.*;
import co.edu.unicauca.question.messaging.CommandPublisher;
import co.edu.unicauca.question.model.*;
import co.edu.unicauca.question.repository.*;
import java.util.*;
import org.springframework.amqp.AmqpException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
@Service public class QuestionService {
 private final QuestionRepository questions;
 private final SubmitRequestRepository submissions;
 private final UserClient users;
 private final ValidationClient validation;
 private final LifecycleClient lifecycle;
 private final CommandPublisher publisher;
 public QuestionService(QuestionRepository questions,SubmitRequestRepository submissions,UserClient users,
  ValidationClient validation,LifecycleClient lifecycle,CommandPublisher publisher){
  this.questions=questions;this.submissions=submissions;this.users=users;this.validation=validation;
  this.lifecycle=lifecycle;this.publisher=publisher;
 }
 public QuestionView create(String authorization,QuestionPayload payload){
  UserProfile user=users.requireAuthor(authorization);
  ensureValid(payload);
  Question saved=questions.save(new Question(payload,user.id()));
  try{lifecycle.initialize(saved.getId());}
  catch(RuntimeException ex){questions.deleteById(saved.getId());throw ex;} // Compensación de creación parcial.
  return view(saved,"DRAFT");
 }
 public QuestionView get(String authorization,Long id){
  users.requireAuthor(authorization);
  Question question=find(id);
  LifecycleView state=lifecycle.state(id);
  return view(question,state.status());
 }
 public QuestionView correct(String authorization,Long id,QuestionPayload payload){
  UserProfile user=users.requireAuthor(authorization);
  Question question=find(id);
  if(!Objects.equals(question.getAuthorId(),user.id()) && !user.roles().contains("ADMIN"))
   throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Solo el autor puede corregir la pregunta");
  if(!"REJECTED".equals(lifecycle.state(id).status()))
   throw new ResponseStatusException(HttpStatus.CONFLICT,"Solo se corrige una pregunta REJECTED");
  ensureValid(payload);
  question.update(payload);questions.save(question);
  lifecycle.transition(id,new TransitionRequest("REJECTED","DRAFT","Pregunta corregida por autor"));
  return view(question,"DRAFT");
 }
 public Map<String,Object> submit(String authorization,Long id){
  UserProfile actor=users.requireAuthor(authorization);
  Question q=find(id);
  if(!Objects.equals(q.getAuthorId(),actor.id()) && !actor.roles().contains("ADMIN"))
   throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Solo el autor puede enviar la pregunta");
  if(!"DRAFT".equals(lifecycle.state(id).status()))
   throw new ResponseStatusException(HttpStatus.CONFLICT,"Solo las preguntas DRAFT pueden enviarse a revisión");
  String operationId=UUID.randomUUID().toString();
  submissions.save(new SubmitRequest(operationId,id));
  try{publisher.submit(id,operationId);}
  catch(AmqpException ex){
   submissions.deleteById(operationId);
   throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"No se pudo publicar mensaje en RabbitMQ");
  }
  return Map.of("message","Solicitud aceptada para procesamiento asíncrono", "operationId",operationId,
   "questionId",id,"status","PROCESSING","checkUrl","/operations/"+operationId);
 }
 public OperationView operation(String authorization,String operationId){
  users.requireAuthor(authorization);
  SubmitRequest request=submissions.findById(operationId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Operación no encontrada"));
  OperationView result=lifecycle.operation(operationId);
  return result!=null?result:new OperationView(operationId,request.getQuestionId(),"PROCESSING","Comando recibido, sin resultado todavía",null);
 }
 private Question find(Long id){return questions.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Pregunta no encontrada"));}
 private void ensureValid(QuestionPayload payload){
  ValidationResult result=validation.validate(payload);
  if(result==null || !result.valid()) throw new InvalidQuestionException(result==null?List.of("Servicio de validación sin respuesta"):result.errors());
 }
 private QuestionView view(Question q,String status){
  return new QuestionView(q.getId(),q.getStatement(),q.getOptions().stream()
   .map(o->new QuestionPayload.OptionInput(o.getOptionId(),o.getText())).toList(),
   q.getCorrectOptionId(),q.getAuthorId(),status);
 }
 public static class InvalidQuestionException extends RuntimeException{
  private final List<String> errors;
  public InvalidQuestionException(List<String> errors){super("Pregunta inválida");this.errors=errors;}
  public List<String> errors(){return errors;}
 }
}
