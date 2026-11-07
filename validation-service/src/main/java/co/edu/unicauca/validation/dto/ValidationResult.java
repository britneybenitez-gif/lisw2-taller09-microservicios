package co.edu.unicauca.validation.dto;
import java.util.List;
public record ValidationResult(boolean valid,List<String> errors) {}
