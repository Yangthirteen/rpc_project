package RpcCore.transport.netty.client;

import RpcCommon.entity.RpcRequest;
import RpcCommon.entity.RpcResponse;
import RpcCommon.enumeration.RpcError;
import RpcCommon.exception.RpcException;
import RpcCommon.util.RpcMessageChecker;
import RpcCore.codec.CommonDecoder;
import RpcCore.codec.CommonEncoder;
import RpcCore.registry.NacosServiceRegistry;
import RpcCore.registry.ServiceRegistry;
import RpcCore.serializer.CommonSerializer;
import RpcCore.serializer.JsonSerializer;
import RpcCore.serializer.KryoSerializer;
import RpcCore.transport.RpcClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

/**
 * NIO方式消费侧客户端类
 * @author tanghong
 */
public class NettyClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private String host;
    private int port;
    private static final Bootstrap bootstrap;
    private final ServiceRegistry serviceRegistry;
    private CommonSerializer serializer;

//    public NettyClient(String host, int port) {
//        this.host = host;
//        this.port = port;
//    }

    public NettyClient() {
        this.serviceRegistry = new NacosServiceRegistry();
    }

    // 初始化相关资源比如 EventLoopGroup, Bootstrap
    static {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new CommonDecoder())
                                .addLast(new CommonEncoder(new KryoSerializer()))//JsonSerializer
                                .addLast(new NettyClientHandler());
                    }
                });
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

//    @Override
//    public Object sendRequest(RpcRequest rpcRequest) {
//        try {
//            ChannelFuture future = bootstrap.connect(host, port).sync();
//            logger.info("客户端连接到服务器 {}:{}", host, port);
//            Channel channel = future.channel();
//            if(channel != null) {
//                channel.writeAndFlush(rpcRequest).addListener(future1 -> {
//                    if(future1.isSuccess()) {
//                        logger.info(String.format("客户端发送消息: %s", rpcRequest.toString()));
//                    } else {
//                        logger.error("发送消息时有错误发生: ", future1.cause());
//                    }
//                });
//                channel.closeFuture().sync();
//                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
//                RpcResponse rpcResponse = channel.attr(key).get();
//                return rpcResponse.getData();
//            }
//
//        } catch (InterruptedException e) {
//            logger.error("发送消息时有错误发生: ", e);
//        }
//        return null;
//    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if(serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        AtomicReference<Object> result = new AtomicReference<>(null);
        try {
            InetSocketAddress inetSocketAddress = serviceRegistry.lookupService(rpcRequest.getInterfaceName());
            Channel channel = ChannelProvider.get(inetSocketAddress, serializer);
            if(channel.isActive()) {
                channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                    if (future1.isSuccess()) {
                        logger.info(String.format("客户端发送消息: %s", rpcRequest.toString()));
                    } else {
                        logger.error("发送消息时有错误发生: ", future1.cause());
                    }
                });
                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse" + rpcRequest.getRequestId());
                RpcResponse rpcResponse = channel.attr(key).get();
                RpcMessageChecker.check(rpcRequest, rpcResponse);
                result.set(rpcResponse.getData());
            } else {
                System.exit(0);
            }
        } catch (InterruptedException e) {
            logger.error("发送消息时有错误发生: ", e);
        }
        return result.get();
    }


}