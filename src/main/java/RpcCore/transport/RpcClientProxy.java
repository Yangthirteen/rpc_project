package RpcCore.transport;

import RpcCommon.entity.RpcRequest;
import RpcCommon.entity.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * RPC客户端动态代理
 * 采用JDK动态代理，代理类是需要实现InvocationHandler接口
 * @author tanghong
 */
public class RpcClientProxy implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);

    private String host;
    private int port;
    private RpcClient client;

//    private final RpcClient client;

    public RpcClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public RpcClientProxy(RpcClient client) {
        this.client = client;
    }

    //获取代理
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        logger.info("调用方法: {}#{}", method.getDeclaringClass().getName(), method.getName());
        RpcRequest rpcRequest = new RpcRequest(UUID.randomUUID().toString(), method.getDeclaringClass().getName(),
                method.getName(), args, method.getParameterTypes());
        return client.sendRequest(rpcRequest);
    }

    //代理调用
    //发送请求
//    @Override
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        logger.info("调用方法: {}#{}", method.getDeclaringClass().getName(), method.getName());
//        RpcRequest rpcRequest = new RpcRequest(method.getDeclaringClass().getName(),
//                method.getName(), args, method.getParameterTypes());
//        return client.sendRequest(rpcRequest);
//    }


//    利用host和port
//    @Override
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        RpcRequest rpcRequest = RpcRequest.builder()
//                .interfaceName(method.getDeclaringClass().getName())
//                .methodName(method.getName())
//                .parameters(args)
//                .paramTypes(method.getParameterTypes())
//                .build();
//        RpcClient rpcClient = new RpcClient();
//        return ((RpcResponse) rpcClient.sendRequest(rpcRequest, host, port)).getData();
//    }
}