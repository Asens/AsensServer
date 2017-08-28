package cn.asens.handler.http;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by Asens on 2017/8/24
 */
public interface Response {
    void sendError(int status);

    void close() throws IOException;

    SocketChannel getChannel();

    void sendOk(long length) throws IOException;

    void write(RandomAccessFile file);

    void flush() throws IOException;

    void writeAndFlush(String message) throws IOException;

    void setContentType(String type);

    void setHeaderEnd();
}
