package co.edu.unicauca.notification.dto;
import java.time.Instant;
public record ReviewerAssignedEvent(String eventId,Long assignmentId,Long questionId,Long reviewerId,Instant occurredAt){}
