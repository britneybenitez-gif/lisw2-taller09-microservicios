package co.edu.unicauca.lifecycle.model;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="question_states") public class QuestionState {
 @Id private Long questionId;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private QuestionStatus status;
 @Version private Long version;
 private Instant updatedAt;
 @Column(length=2000) private String lastReason;
 protected QuestionState() {}
 public QuestionState(Long questionId) { this.questionId=questionId; this.status=QuestionStatus.DRAFT; this.updatedAt=Instant.now(); }
 public Long getQuestionId() { return questionId; }
 public QuestionStatus getStatus() { return status; }
 public Instant getUpdatedAt() { return updatedAt; }
 public String getLastReason() { return lastReason; }
 public void transition(QuestionStatus next,String reason) { this.status=next;this.lastReason=reason;this.updatedAt=Instant.now(); }
}
