package co.edu.unicauca.notification.model;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="notifications") public class Notification {
 @Id private String eventId;
 private Long questionId;
 private Long reviewerId;
 private Long assignmentId;
 @Column(length=1500) private String message;
 private String deliveryStatus;
 private Instant sentAt;
 protected Notification(){}
 public Notification(String eventId,Long assignmentId,Long questionId,Long reviewerId,String message){
  this.eventId=eventId;this.assignmentId=assignmentId;this.questionId=questionId;this.reviewerId=reviewerId;
  this.message=message;this.deliveryStatus="SENT_SIMULATED";this.sentAt=Instant.now();
 }
 public String getEventId(){return eventId;}
 public Long getQuestionId(){return questionId;}
 public Long getReviewerId(){return reviewerId;}
 public Long getAssignmentId(){return assignmentId;}
 public String getMessage(){return message;}
 public String getDeliveryStatus(){return deliveryStatus;}
 public Instant getSentAt(){return sentAt;}
}
