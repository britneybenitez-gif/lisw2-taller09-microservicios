package co.edu.unicauca.lifecycle.dto;
import co.edu.unicauca.lifecycle.model.QuestionStatus;
public record TransitionRequest(QuestionStatus expectedStatus,QuestionStatus targetStatus,String reason) {}
