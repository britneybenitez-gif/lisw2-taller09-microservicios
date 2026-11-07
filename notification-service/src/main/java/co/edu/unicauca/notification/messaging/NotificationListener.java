package co.edu.unicauca.notification.messaging;
import co.edu.unicauca.notification.dto.ReviewerAssignedEvent;
import co.edu.unicauca.notification.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
@Component public class NotificationListener {
 private final EmailService email;
 public NotificationListener(EmailService email){this.email=email;}
 @RabbitListener(queues="notification.reviewer.assigned.queue") public void onReviewerAssigned(ReviewerAssignedEvent event){email.simulate(event);}
}
