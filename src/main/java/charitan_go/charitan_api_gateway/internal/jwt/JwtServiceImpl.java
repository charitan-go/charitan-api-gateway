package charitan_go.charitan_api_gateway.internal.jwt;

import charitan_go.charitan_api_gateway.external.key.grpc.KeyGrpcClient;
import charitan_go.charitan_api_gateway.pkg.proto.GetPublicKeyResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class JwtServiceImpl implements JwtService{
    @Autowired
    private KeyGrpcClient keyGrpcClient;

    @Override
    public void handleGetPublicKeyRabbitmq() {
        GetPublicKeyResponseDto getPublicKeyResponseDto = keyGrpcClient.getPublicKey();
        System.out.println(getPublicKeyResponseDto.getPublicKey());
    }
}
