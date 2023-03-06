package RpcCore.registry;

import RpcCommon.enumeration.RpcError;
import RpcCommon.exception.RpcException;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Nacos服务注册中心
 * @author tanghong
 */
public class NacosServiceRegistry implements ServiceRegistry {

    private static final Logger logger = LoggerFactory.getLogger(NacosServiceRegistry.class);

    private static final String SERVER_ADDR = "127.0.0.1:8848";//nacos地址
    private static final NamingService namingService;//namingService 提供了两个很方便的接口，registerInstance 和 getAllInstances 方法，前者可以直接向 Nacos 注册服务，后者可以获得提供某个服务的所有提供者的列表。

    static {
        try {
            namingService = NamingFactory.createNamingService(SERVER_ADDR);
            logger.error("成功连接到nacos");
        } catch (NacosException e) {
            logger.error("连接到Nacos时有错误发生: ", e);
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    @Override
    public <T> void register(T service) {

    }

    @Override
    public Object getService(String serviceName) {
        return null;
    }

    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            namingService.registerInstance(serviceName, inetSocketAddress.getHostName(), inetSocketAddress.getPort());
            logger.error(serviceName+"服务注册成功");
        } catch (NacosException e) {
            logger.error("注册服务时有错误发生:", e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<Instance> instances = namingService.getAllInstances(serviceName);//获取所有同名服务
            Instance instance = instances.get(0);//从同名服务中选取一个，这里默认选取第一个
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            logger.error("获取服务时有错误发生:", e);
        }
        return null;
    }
}
