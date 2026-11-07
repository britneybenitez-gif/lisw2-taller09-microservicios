package co.edu.unicauca.question.model;
import co.edu.unicauca.question.dto.QuestionPayload;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;
@Entity @Table(name="questions") public class Question {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=4000) private String statement;
 @ElementCollection(fetch=FetchType.EAGER)
 @CollectionTable(name="question_options",joinColumns=@JoinColumn(name="question_id"))
 @OrderColumn(name="position_index")
 private List<OptionValue> options=new ArrayList<>();
 @Column(nullable=false) private String correctOptionId;
 @Column(nullable=false) private Long authorId;
 private Instant createdAt;
 protected Question(){}
 public Question(QuestionPayload payload,Long authorId){this.authorId=authorId;update(payload);this.createdAt=Instant.now();}
 public void update(QuestionPayload payload){
  this.statement=payload.statement();this.correctOptionId=payload.correctOptionId();
  this.options.clear();
  for(QuestionPayload.OptionInput item:payload.options()) this.options.add(new OptionValue(item.id(),item.text()));
 }
 public Long getId(){return id;}
 public String getStatement(){return statement;}
 public List<OptionValue> getOptions(){return options;}
 public String getCorrectOptionId(){return correctOptionId;}
 public Long getAuthorId(){return authorId;}
}
