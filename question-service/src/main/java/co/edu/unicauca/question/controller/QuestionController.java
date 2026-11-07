package co.edu.unicauca.question.controller;
import co.edu.unicauca.question.dto.*;
import co.edu.unicauca.question.service.QuestionService;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@RestController public class QuestionController {
 private final QuestionService service;
 public QuestionController(QuestionService service){this.service=service;}
 @PostMapping("/questions") public ResponseEntity<QuestionView> create(
  @RequestHeader(value="Authorization",required=false) String auth,@RequestBody QuestionPayload payload){
  return ResponseEntity.status(HttpStatus.CREATED).body(service.create(auth,payload));
 }
 @GetMapping("/questions/{id}") public QuestionView find(
  @RequestHeader(value="Authorization",required=false) String auth,@PathVariable Long id){return service.get(auth,id);}
 @PutMapping("/questions/{id}") public QuestionView correct(
  @RequestHeader(value="Authorization",required=false) String auth,@PathVariable Long id,@RequestBody QuestionPayload payload){
  return service.correct(auth,id,payload);
 }
 @PostMapping("/questions/{id}/submit") public ResponseEntity<Map<String,Object>> submit(
  @RequestHeader(value="Authorization",required=false) String auth,@PathVariable Long id){
  return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.submit(auth,id));
 }
 @GetMapping("/operations/{operationId}") public OperationView operation(
  @RequestHeader(value="Authorization",required=false) String auth,@PathVariable String operationId){
  return service.operation(auth,operationId);
 }
}
