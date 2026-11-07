package co.edu.unicauca.question.config;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.*;
@Configuration public class RabbitMQConfig {
 public static final String EXCHANGE="saberpro.direct.exchange";
 public static final String QUEUE="question.submit.review.queue";
 public static final String ROUTING_KEY="question.submit";
 @Bean public DirectExchange exchange(){return new DirectExchange(EXCHANGE,true,false);}
 @Bean public Queue submitQueue(){return QueueBuilder.durable(QUEUE).build();}
 @Bean public Binding submitBinding(Queue submitQueue,DirectExchange exchange){return BindingBuilder.bind(submitQueue).to(exchange).with(ROUTING_KEY);}
 @Bean public MessageConverter jsonConverter(){return new Jackson2JsonMessageConverter();}
}
