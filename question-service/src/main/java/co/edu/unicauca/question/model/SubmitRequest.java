package co.edu.unicauca.question.model;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="submit_requests") public class SubmitRequest {
 @Id private String operationId;
 private Long questionId;
 private Instant acceptedAt;
 protected SubmitRequest(){}
 public SubmitRequest(String operationId,Long questionId){this.operationId=operationId;this.questionId=questionId;this.acceptedAt=Instant.now();}
 public String getOperationId(){return operationId;}
 public Long getQuestionId(){return questionId;}
 public Instant getAcceptedAt(){return acceptedAt;}
}
