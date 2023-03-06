package RpcCore.transport;

import RpcCore.serializer.CommonSerializer;

public interface RpcServer {
    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    void start(int port);
    void start();
    void setSerializer(CommonSerializer serializer);

    //用于向 Nacos 注册服务
    //<T> void publishService(Object service, Class<T> serviceClass);
    <T> void publishService(T service, String serviceName);

}
