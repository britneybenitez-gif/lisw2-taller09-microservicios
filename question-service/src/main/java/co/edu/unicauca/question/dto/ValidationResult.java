package co.edu.unicauca.question.dto;
import java.util.List;
public record ValidationResult(boolean valid,List<String> errors){}
