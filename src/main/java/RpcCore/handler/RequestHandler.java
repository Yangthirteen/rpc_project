package RpcCore.handler;

import RpcCommon.entity.RpcRequest;
import RpcCommon.entity.RpcResponse;
import RpcCommon.enumeration.ResponseCode;
import RpcCore.provider.ServiceProvider;
import RpcCore.provider.ServiceProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 进行过程调用的处理器
 * @author tanghong
 */
//public class RequestHandler {
//
//    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
//
//    public Object handle(RpcRequest rpcRequest, Object service) {
//        Object result = null;
//        try {
//            result = invokeTargetMethod(rpcRequest, service);
//            logger.info("服务:{} 成功调用方法:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            logger.error("调用或发送时有错误发生：", e);
//        } return result;
//    }
//
//    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) throws IllegalAccessException, InvocationTargetException {
//        Method method;
//        try {
//            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
//        } catch (NoSuchMethodException e) {
//            return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND);
//        }
//        return method.invoke(service, rpcRequest.getParameters());
//    }
//
//}

/**
 * 进行过程调用的处理器
 * 加入nacos
 * @author tanghong
 */
public class RequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final ServiceProvider serviceProvider;

    static {
        serviceProvider = new ServiceProviderImpl();
    }

    public Object handle(RpcRequest rpcRequest, Object service) {
        Object result = null;
        try {
            result = invokeTargetMethod(rpcRequest, service);
            logger.info("服务:{} 成功调用方法:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("调用或发送时有错误发生：", e);
        } return result;
    }

    public Object handle(RpcRequest rpcRequest) {
        Object result = null;
        Object service = serviceProvider.getServiceProvider(rpcRequest.getInterfaceName());
        try {
            result = invokeTargetMethod(rpcRequest, service);
            logger.info("服务:{} 成功调用方法:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("调用或发送时有错误发生：", e);
        }
        return result;
    }

    //调用目标方法
    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) throws IllegalAccessException, InvocationTargetException {
        Method method;
        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
        } catch (NoSuchMethodException e) {
            return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND, rpcRequest.getRequestId());
        }
        return method.invoke(service, rpcRequest.getParameters());
    }

}