package RpcCore.transport.netty.server;

import RpcCommon.entity.RpcRequest;
import RpcCommon.entity.RpcResponse;
import RpcCommon.factory.ThreadPoolFactory;
import RpcCore.handler.RequestHandler;
import RpcCore.registry.DefaultServiceRegistry;
import RpcCore.registry.ServiceRegistry;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;


/**
 * Netty中处理RpcRequest的Handler
 * @author tanghong
 */
//public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
//
//    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
//    private static RequestHandler requestHandler;
//    private static ServiceRegistry serviceRegistry;
//
//    static {
//        requestHandler = new RequestHandler();
//        serviceRegistry = new DefaultServiceRegistry();
//    }
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
//        try {
//            logger.info("服务器接收到请求: {}", msg);
//            String interfaceName = msg.getInterfaceName();
//            Object service = serviceRegistry.getService(interfaceName);
//            Object result = requestHandler.handle(msg, service);
//            ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(result));
//            future.addListener(ChannelFutureListener.CLOSE);
//        } finally {
//            ReferenceCountUtil.release(msg);
//        }
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        logger.error("处理过程调用时有错误发生:");
//        cause.printStackTrace();
//        ctx.close();
//    }
//
//}


/**
 * Netty中处理RpcRequest的Handler
 * 加入nacos
 * @author tanghong
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static RequestHandler requestHandler;
    private static final String THREAD_NAME_PREFIX = "netty-server-handler";
    private static final ExecutorService threadPool;

    static {
        requestHandler = new RequestHandler();
        threadPool = ThreadPoolFactory.createDefaultThreadPool(THREAD_NAME_PREFIX);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        threadPool.execute(() -> {
            try {
                logger.info("服务器接收到请求: {}", msg);
                Object result = requestHandler.handle(msg);
                ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(result, msg.getRequestId()));
                future.addListener(ChannelFutureListener.CLOSE);
            } finally {
                ReferenceCountUtil.release(msg);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }

}