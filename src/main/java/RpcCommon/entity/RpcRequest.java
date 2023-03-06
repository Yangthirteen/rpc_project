package RpcCommon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

/**
 * 消费者向提供者发送的请求对象
 * 服务端需要以下信息，才能唯一确定服务端需要调用的接口的方法
 * 四个信息
 *
 * @author tonghong
 */
@Data
@AllArgsConstructor
public class RpcRequest implements Serializable {

    public RpcRequest() {}

    /**
     * 请求号
     */
    private String requestId;
    /**
     * 待调用接口名称
     */
    private String interfaceName;

    /**
     * 待调用方法名称
     */
    private String methodName;

    /**
     * 调用方法的参数
     */
    private Object[] parameters;

    /**
     * 调用方法的参数类型
     */
    private Class<?>[] paramTypes;

}
