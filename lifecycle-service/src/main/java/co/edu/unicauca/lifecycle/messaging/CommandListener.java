package co.edu.unicauca.lifecycle.messaging;
import co.edu.unicauca.lifecycle.dto.SubmitQuestionCommand;
import co.edu.unicauca.lifecycle.service.LifecycleService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
@Component public class CommandListener {
 private final LifecycleService lifecycle;
 public CommandListener(LifecycleService lifecycle){this.lifecycle=lifecycle;}
 @RabbitListener(queues="question.submit.review.queue")
 public void handleSubmitForReview(SubmitQuestionCommand command) { lifecycle.processSubmit(command); }
}
