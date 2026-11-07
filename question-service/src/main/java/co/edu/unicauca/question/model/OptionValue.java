package co.edu.unicauca.question.model;
import jakarta.persistence.Embeddable;
@Embeddable public class OptionValue {
 private String optionId;
 @jakarta.persistence.Column(length=2000) private String text;
 protected OptionValue(){}
 public OptionValue(String id,String text){this.optionId=id;this.text=text;}
 public String getOptionId(){return optionId;}
 public String getText(){return text;}
}
