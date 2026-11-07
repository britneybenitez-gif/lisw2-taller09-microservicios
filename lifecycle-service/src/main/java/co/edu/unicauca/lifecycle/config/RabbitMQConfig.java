package co.edu.unicauca.lifecycle.config;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.*;
@Configuration public class RabbitMQConfig {
 public static final String EXCHANGE="saberpro.direct.exchange";
 public static final String QUEUE="question.submit.review.queue";
 @Bean public DirectExchange submitExchange(){return new DirectExchange(EXCHANGE,true,false);}
 @Bean public Queue submitQueue(){return QueueBuilder.durable(QUEUE).build();}
 @Bean public Binding submitBinding(Queue submitQueue,DirectExchange submitExchange){
  return BindingBuilder.bind(submitQueue).to(submitExchange).with("question.submit");
 }
 @Bean public MessageConverter jsonConverter(){return new Jackson2JsonMessageConverter();}
}
