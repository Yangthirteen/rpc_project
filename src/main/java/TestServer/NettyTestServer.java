package TestServer;

import RpcApi.HelloService;
import RpcCore.annotation.ServiceScan;
import RpcCore.registry.DefaultServiceRegistry;
import RpcCore.registry.ServiceRegistry;
import RpcCore.serializer.CommonSerializer;
import RpcCore.serializer.ProtobufSerializer;
import RpcCore.transport.netty.server.NettyServer;

/**
 * 测试用Netty服务提供者（服务端）
 * @author tanghong
 */
@ServiceScan
public class NettyTestServer {


//    public static void main(String[] args) {
//        HelloService helloService = new HelloServiceImpl();
//        ServiceRegistry registry = new DefaultServiceRegistry();
//        registry.register(helloService);
//        NettyServer server = new NettyServer();
//        server.start(9999);
//    }


    //加入nacos测试
//    public static void main(String[] args) {
//        HelloService helloService = new HelloServiceImpl();
//        NettyServer server = new NettyServer("127.0.0.1", 9999);
//        //server.setSerializer(new ProtobufSerializer());
//        server.publishService(helloService, HelloService.class);
//    }

    public static void main(String[] args) {
        NettyServer server = new NettyServer("127.0.0.1", 9999, CommonSerializer.PROTOBUF_SERIALIZER);
        server.start();
    }

}
