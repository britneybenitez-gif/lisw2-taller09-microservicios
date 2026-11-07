package co.edu.unicauca.review.dto;
import java.time.Instant;
public record ReviewerAssignedEvent(String eventId,Long assignmentId,Long questionId,Long reviewerId,Instant occurredAt){}
