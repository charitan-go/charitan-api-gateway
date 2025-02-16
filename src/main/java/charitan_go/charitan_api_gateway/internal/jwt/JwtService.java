package charitan_go.charitan_api_gateway.internal.jwt;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface JwtService {
    void handleGetPublicKeyRabbitmq() throws NoSuchAlgorithmException, InvalidKeySpecException;
}
