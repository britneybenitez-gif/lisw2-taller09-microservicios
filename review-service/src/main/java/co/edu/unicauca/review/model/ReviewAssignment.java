package co.edu.unicauca.review.model;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="review_assignments") public class ReviewAssignment {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="question_id",nullable=false) private Long questionId;
 private Long reviewerId;
 private String status;
 @Column(nullable=false,unique=true) private String eventId;
 private String decision;
 @Column(length=1500) private String reason;
 private Instant assignedAt;
 protected ReviewAssignment(){}
 public ReviewAssignment(Long questionId,Long reviewerId){
  this.questionId=questionId;this.reviewerId=reviewerId;this.status="RESERVED";
  this.eventId=UUID.randomUUID().toString();this.assignedAt=Instant.now();
 }
 public Long getId(){return id;}
 public Long getQuestionId(){return questionId;}
 public Long getReviewerId(){return reviewerId;}
 public String getStatus(){return status;}
 public String getEventId(){return eventId;}
 public String getDecision(){return decision;}
 public String getReason(){return reason;}
 public Instant getAssignedAt(){return assignedAt;}
 public void status(String status){this.status=status;}
 public void decide(String decision,String reason){this.decision=decision;this.reason=reason;this.status="DECIDED";}
}
