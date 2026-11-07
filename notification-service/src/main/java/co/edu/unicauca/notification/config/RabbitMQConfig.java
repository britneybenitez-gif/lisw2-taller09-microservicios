package co.edu.unicauca.notification.config;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.*;
@Configuration public class RabbitMQConfig {
 public static final String EXCHANGE="saberpro.events.exchange";
 public static final String QUEUE="notification.reviewer.assigned.queue";
 @Bean public TopicExchange eventExchange(){return new TopicExchange(EXCHANGE,true,false);}
 @Bean public Queue notificationQueue(){return QueueBuilder.durable(QUEUE).build();}
 @Bean public Binding reviewerAssignedBinding(Queue notificationQueue,TopicExchange eventExchange){
  return BindingBuilder.bind(notificationQueue).to(eventExchange).with("reviewer.assigned");
 }
 @Bean public MessageConverter jsonConverter(){return new Jackson2JsonMessageConverter();}
}
