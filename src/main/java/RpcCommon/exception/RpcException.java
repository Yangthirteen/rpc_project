package RpcCommon.exception;

import RpcCommon.enumeration.RpcError;

/**
 * RPC调用异常
 *
 * @author tanghong
 */
public class RpcException extends RuntimeException {

    public RpcException(RpcError error, String detail) {
        super(error.getMessage() + ": " + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcError error) {
        super(error.getMessage());
    }

}