package cn.asens.handler.http;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by Asens on 2017/8/24
 */
public interface Response {
    void sendError(int status);

    void send(String message) throws IOException;

    void send(byte[] bytes) throws IOException;

    void close() throws IOException;

    SocketChannel getChannel();

    void sendOk() throws IOException;

    void send(byte[] bytes, int index, int length) throws IOException;

    void send(ByteBuffer[] arr) throws IOException;
}
