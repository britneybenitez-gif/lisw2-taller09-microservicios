package co.edu.unicauca.question.controller;
import co.edu.unicauca.question.service.QuestionService.InvalidQuestionException;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@RestControllerAdvice public class QuestionExceptionHandler {
 @ExceptionHandler(InvalidQuestionException.class)
 public ResponseEntity<Map<String,Object>> invalid(InvalidQuestionException e){
  return ResponseEntity.badRequest().body(Map.of("message","Pregunta inválida","errors",e.errors()));
 }
}
