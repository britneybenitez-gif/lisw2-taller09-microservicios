package co.edu.unicauca.lifecycle.controller;
import co.edu.unicauca.lifecycle.dto.TransitionRequest;
import co.edu.unicauca.lifecycle.model.*;
import co.edu.unicauca.lifecycle.service.LifecycleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController public class LifecycleController {
 private final LifecycleService service;
 public LifecycleController(LifecycleService service){this.service=service;}
 @PostMapping("/lifecycle/{id}/initialize") @ResponseStatus(HttpStatus.CREATED)
 public QuestionState initialize(@PathVariable Long id){return service.initialize(id);}
 @GetMapping("/lifecycle/{id}") public QuestionState get(@PathVariable Long id){return service.get(id);}
 @PostMapping("/lifecycle/{id}/transition") public QuestionState transition(@PathVariable Long id,@RequestBody TransitionRequest request){
  return service.transition(id,request);
 }
 @GetMapping("/operations/{operationId}") public OperationResult operation(@PathVariable String operationId){return service.getOperation(operationId);}
}
