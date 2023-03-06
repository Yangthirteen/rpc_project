package RpcCore.transport.socket.client;

import RpcCommon.entity.RpcRequest;
import RpcCore.serializer.CommonSerializer;
import RpcCore.transport.RpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

//就是将一个对象发过去，并且接受返回的对象

public class SocketClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    public Object sendRequest(RpcRequest rpcRequest, String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            return objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            logger.error("调用时有错误发生：", e);
            return null;
        }
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        return null;
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {

    }
}

