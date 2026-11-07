package co.edu.unicauca.review.messaging;
import co.edu.unicauca.review.config.RabbitMQConfig;
import co.edu.unicauca.review.dto.ReviewerAssignedEvent;
import co.edu.unicauca.review.model.ReviewAssignment;
import java.time.Instant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
@Component public class EventPublisher {
 private final RabbitTemplate rabbit;
 public EventPublisher(RabbitTemplate rabbit){this.rabbit=rabbit;}
 public void assigned(ReviewAssignment a){
  rabbit.convertAndSend(RabbitMQConfig.EXCHANGE,RabbitMQConfig.ROUTING_KEY,
   new ReviewerAssignedEvent(a.getEventId(),a.getId(),a.getQuestionId(),a.getReviewerId(),Instant.now()));
 }
}
