package charitan_go.charitan_api_gateway.external.key.grpc;

import charitan_go.charitan_api_gateway.pkg.proto.CharitanApiGatewayProto;
import charitan_go.charitan_api_gateway.pkg.proto.GetPublicKeyRequestDto;
import charitan_go.charitan_api_gateway.pkg.proto.GetPublicKeyResponseDto;
import org.springframework.stereotype.Service;

public interface KeyGrpcClient {
   //Charitanapigateway.GetPublicKeyRequestDto GetPublicKey(Charitanapigateway.GetPublicKeyRequestDto getPublicKeyRequestDto);
    GetPublicKeyResponseDto getPublicKey();
}
