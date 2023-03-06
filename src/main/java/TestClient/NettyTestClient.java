package TestClient;

import RpcApi.HelloService;
import RpcCore.loadbalancer.LoadBalancer;
import RpcCore.loadbalancer.RandomLoadBalancer;
import RpcCore.registry.NacosServiceDiscovery;
import RpcCore.serializer.ProtobufSerializer;
import RpcCore.transport.RpcClient;
import RpcCore.transport.RpcClientProxy;
import RpcCore.transport.netty.client.NettyClient;
import RpcApi.HelloObject;
import RpcCore.registry.ServiceDiscovery;

import java.net.InetSocketAddress;

/**
 * 测试用Netty消费者
 * @author tanghong
 */
public class NettyTestClient {

//    public static void main(String[] args) {
//        RpcClient client = new NettyClient("127.0.0.1", 9999);
//        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
//        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
//        HelloObject object = new HelloObject(12, "This is a message");
//        String res = helloService.hello(object);
//        System.out.println(res);
//    }

    //加入nacos的测试
    public static void main(String[] args) {
        RpcClient client = new NettyClient();
        client.setSerializer(new ProtobufSerializer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);

        LoadBalancer loadBalancer = new RandomLoadBalancer();
        ServiceDiscovery serviceDiscovery = new NacosServiceDiscovery(loadBalancer);
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService("RpcApi.HelloService");

        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }

}