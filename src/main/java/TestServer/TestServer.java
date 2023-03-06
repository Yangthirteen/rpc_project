package TestServer;


import RpcApi.HelloService;
import RpcCore.registry.DefaultServiceRegistry;
import RpcCore.registry.ServiceRegistry;
import RpcCore.transport.RpcServer;
import RpcCore.transport.socket.server.SocketServer;

/**
 * 测试用服务提供方（服务端）
 * @author tonghong
 */
public class TestServer {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        SocketServer rpcServer = new SocketServer(serviceRegistry);
        rpcServer.start(9000);
    }

}