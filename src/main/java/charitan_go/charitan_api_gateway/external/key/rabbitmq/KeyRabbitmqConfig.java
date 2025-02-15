package charitan_go.charitan_api_gateway.external.key.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeyRabbitmqConfig {

    public static final String EXCHANGE_NAME = "key.exchange";
    public static final String QUEUE_NAME = "charitan_api_gateway.public_key.queue";
    public static final String ROUTING_KEY = "key.get_public_key";


    @Bean
    public TopicExchange keyExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue charitanApiGatewayPublicKeyQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Binding getPublicKeyRoutingKey(Queue charitanApiGatewayPublicKeyQueue, TopicExchange keyExchange) {
        return BindingBuilder.bind(charitanApiGatewayPublicKeyQueue)
                .to(keyExchange)
                .with(ROUTING_KEY);
    }
}
