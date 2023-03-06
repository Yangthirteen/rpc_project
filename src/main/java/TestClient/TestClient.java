package TestClient;


import RpcApi.HelloObject;
import RpcApi.HelloService;
import RpcCore.transport.RpcClientProxy;
import RpcCore.transport.socket.client.SocketClient;

/**
 * 测试用消费者（客户端）
 * @author tanghong
 */
public class TestClient {

    public static void main(String[] args) {
        SocketClient client = new SocketClient();
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }

}