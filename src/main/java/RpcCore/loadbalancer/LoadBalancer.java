package RpcCore.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;
import java.util.List;

/**
 * @author tanghong
 */
public interface LoadBalancer {

    //用于从一系列 Instance 中选择一个，实现两个比较经典的算法：随机和转轮。
    Instance select(List<Instance> instances);

}
