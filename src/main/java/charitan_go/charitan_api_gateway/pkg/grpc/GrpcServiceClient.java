package charitan_go.charitan_api_gateway.pkg.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrpcServiceClient {
    private final DiscoveryClient discoveryClient;

    @Autowired
    public GrpcServiceClient(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    /**
     * Discover the service URI for the given serviceId.
     */
    public String discoverServiceUri(String serviceId) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        if (instances.isEmpty()) {
            throw new IllegalStateException("No instances available for service: " + serviceId);
        }
        // For simplicity, select the first instance.
        ServiceInstance instance = instances.get(0);
        return instance.getHost() + ":" + instance.getPort();
    }

    /**
     * Build a gRPC channel for the Golang key service.
     */
    public ManagedChannel buildGrpcChannel(String serviceId) {
        String uri = discoverServiceUri(serviceId);
        System.out.println("Target URI is: " + uri);
        // Assumes the service is running with plaintext gRPC (adjust for TLS if necessary)
        return ManagedChannelBuilder.forTarget(uri)
                .usePlaintext()
                .build();
    }
}
