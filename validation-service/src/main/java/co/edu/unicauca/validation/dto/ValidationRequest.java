package co.edu.unicauca.validation.dto;
import java.util.List;
public record ValidationRequest(String statement,List<OptionInput> options,String correctOptionId) {
 public record OptionInput(String id,String text) {}
}
