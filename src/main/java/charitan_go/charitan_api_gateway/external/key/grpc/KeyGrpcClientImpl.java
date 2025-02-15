package charitan_go.charitan_api_gateway.external.key.grpc;

import charitan_go.charitan_api_gateway.pkg.proto.GetPublicKeyRequestDto;
import charitan_go.charitan_api_gateway.pkg.proto.GetPublicKeyResponseDto;
import charitan_go.charitan_api_gateway.pkg.proto.KeyGrpcServiceGrpc;
import com.rabbitmq.client.AMQP;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
class KeyGrpcClientImpl implements KeyGrpcClient {
    @GrpcClient("key-grpc-service")
    private KeyGrpcServiceGrpc.KeyGrpcServiceBlockingStub keyServiceStub;

    @Override
    public GetPublicKeyResponseDto getPublicKey() {
        GetPublicKeyRequestDto requestDto = GetPublicKeyRequestDto.newBuilder().build();
        return keyServiceStub.getPublicKey(requestDto);
    }
}
