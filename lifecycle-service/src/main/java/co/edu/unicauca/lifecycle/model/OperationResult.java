package co.edu.unicauca.lifecycle.model;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="operation_results") public class OperationResult {
 @Id private String operationId;
 @Column(nullable=false) private Long questionId;
 @Column(nullable=false) private String status;
 @Column(length=1500) private String detail;
 private Instant processedAt;
 protected OperationResult() {}
 public OperationResult(String id,Long questionId,String status,String detail) {
  this.operationId=id;this.questionId=questionId;this.status=status;this.detail=detail;this.processedAt=Instant.now();
 }
 public String getOperationId(){return operationId;}
 public Long getQuestionId(){return questionId;}
 public String getStatus(){return status;}
 public String getDetail(){return detail;}
 public Instant getProcessedAt(){return processedAt;}
}
