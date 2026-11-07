package co.edu.unicauca.question.messaging;
import co.edu.unicauca.question.config.RabbitMQConfig;
import co.edu.unicauca.question.dto.SubmitQuestionCommand;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
@Component public class CommandPublisher {
 private final RabbitTemplate rabbit;
 public CommandPublisher(RabbitTemplate rabbit){this.rabbit=rabbit;}
 public void submit(Long questionId,String operationId){
  rabbit.convertAndSend(RabbitMQConfig.EXCHANGE,RabbitMQConfig.ROUTING_KEY,new SubmitQuestionCommand(questionId,operationId));
 }
}
