package charitan_go.charitan_api_gateway.internal.jwt;

import charitan_go.charitan_api_gateway.external.key.grpc.KeyGrpcClient;
import charitan_go.charitan_api_gateway.pkg.proto.GetPublicKeyResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
class JwtServiceImpl implements JwtService{
    private static final Logger log = LoggerFactory.getLogger(JwtServiceImpl.class);
    private final KeyGrpcClient keyGrpcClient;

    private PublicKey publicKey;

    @Autowired
    JwtServiceImpl(KeyGrpcClient keyGrpcClient) {
        this.keyGrpcClient = keyGrpcClient;
    }

    private void savePublicKey(String keyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {

        // Remove PEM headers and whitespace
        String publicKeyPEM = keyStr
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        // Base64-decode the result
        byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);

        // Generate the PublicKey object
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        publicKey = keyFactory.generatePublic(spec);
    }


    @Override
    public void handleGetPublicKeyRabbitmq() {
        // Get public key via GRPC
        GetPublicKeyResponseDto getPublicKeyResponseDto = keyGrpcClient.getPublicKey();

        log.info("Get public key successfully from key-server-grpc");

        // Save public key
        try {
            savePublicKey(getPublicKeyResponseDto.getPublicKey());
            log.info("Save public key success");
        } catch (Exception e) {
            log.error("Error in saving public key");
        }
    }
}
