package charitan_go.charitan_api_gateway.external.key.grpc;

import charitan_go.charitan_api_gateway.pkg.grpc.GrpcServiceClient;
import charitan_go.charitan_api_gateway.pkg.proto.GetPublicKeyRequestDto;
import charitan_go.charitan_api_gateway.pkg.proto.GetPublicKeyResponseDto;
import charitan_go.charitan_api_gateway.pkg.proto.KeyGrpcServiceGrpc;
import com.rabbitmq.client.AMQP;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class KeyGrpcClientImpl implements KeyGrpcClient {
    private static final String KEY_GRPC_SERVER_URI = "key-server-grpc";

    private final GrpcServiceClient grpcServiceClient;

    @Autowired
    KeyGrpcClientImpl(GrpcServiceClient grpcServiceClient) {
        this.grpcServiceClient = grpcServiceClient;
    }

    @Override
    public GetPublicKeyResponseDto getPublicKey() {
        // Discover the service and build the channel.
        ManagedChannel channel = grpcServiceClient.buildGrpcChannel(KEY_GRPC_SERVER_URI);
        KeyGrpcServiceGrpc.KeyGrpcServiceBlockingStub stub = KeyGrpcServiceGrpc.newBlockingStub(channel);

        // Build and send the request.
        GetPublicKeyRequestDto request = GetPublicKeyRequestDto.newBuilder().build();
        GetPublicKeyResponseDto response = stub.getPublicKey(request);

        // Shutdown the channel after the call.
        channel.shutdown();

        return response;
    }
}
