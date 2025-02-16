package charitan_go.charitan_api_gateway.external.key.rabbitmq;

import charitan_go.charitan_api_gateway.internal.jwt.JwtService;
import charitan_go.charitan_api_gateway.pkg.proto.GetPublicKeyResponseDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Component
class KeyRabbitmqConsumer {

    @Autowired
    private JwtService jwtService;

    @RabbitListener(queues = KeyRabbitmqConfig.QUEUE_NAME)
    void handleGetPublicKeyRabbitmq(String message) throws NoSuchAlgorithmException, InvalidKeySpecException {
        System.out.println(message);
        jwtService.handleGetPublicKeyRabbitmq();
    }
}
