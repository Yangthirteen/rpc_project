package RpcCore.transport;

import RpcCommon.entity.RpcRequest;
import RpcCore.serializer.CommonSerializer;

public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);
    public void setSerializer(CommonSerializer serializer);
}
