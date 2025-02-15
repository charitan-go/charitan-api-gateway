package charitan_go.charitan_api_gateway.external.key.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class KeyRabbitmqConsumer {

    @RabbitListener(queues = KeyRabbitmqConfig.QUEUE_NAME)
    public void handleGetPublicKeyRabbitmq(String message) {
        System.out.println(message);
    }
}
