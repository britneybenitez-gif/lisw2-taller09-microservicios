package co.edu.unicauca.review.controller;
import co.edu.unicauca.review.dto.*;
import co.edu.unicauca.review.model.ReviewAssignment;
import co.edu.unicauca.review.service.ReviewService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/reviews") public class ReviewController {
 private final ReviewService service;
 public ReviewController(ReviewService service){this.service=service;}
 @PostMapping("/assign") public ResponseEntity<ReviewAssignment> assign(
  @RequestHeader(value="Authorization",required=false) String auth,@RequestBody AssignRequest req){
  ReviewAssignment a=service.assign(auth,req);
  return ResponseEntity.status("ACTIVE".equals(a.getStatus())?HttpStatus.CREATED:HttpStatus.ACCEPTED).body(a);
 }
 @GetMapping public List<ReviewAssignment> list(){return service.list();}
 @GetMapping("/{id}") public ReviewAssignment get(@PathVariable Long id){return service.find(id);}
 @PostMapping("/{id}/reconcile") public ReviewAssignment reconcile(
  @RequestHeader(value="Authorization",required=false) String auth,@PathVariable Long id){return service.reconcile(auth,id);}
 @PostMapping("/{id}/retry-notification") public ReviewAssignment retry(
  @RequestHeader(value="Authorization",required=false) String auth,@PathVariable Long id){return service.retryNotification(auth,id);}
 @PostMapping("/{id}/decision") public ReviewAssignment decide(
  @RequestHeader(value="Authorization",required=false) String auth,@PathVariable Long id,@RequestBody ReviewDecision req){
  return service.decide(auth,id,req);
 }
}
