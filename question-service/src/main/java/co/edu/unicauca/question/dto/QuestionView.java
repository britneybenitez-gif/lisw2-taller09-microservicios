package co.edu.unicauca.question.dto;
import java.util.List;
public record QuestionView(Long id,String statement,List<QuestionPayload.OptionInput> options,String correctOptionId,Long authorId,String status){}
