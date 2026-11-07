package co.edu.unicauca.question.dto;
import java.util.List;
public record QuestionPayload(String statement,List<OptionInput> options,String correctOptionId){
 public record OptionInput(String id,String text){}
}
