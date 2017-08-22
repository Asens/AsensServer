package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Asens on 2017/4/18.
 */
public class NioServer {
    final Selector selector=Selector.open();

    public NioServer() throws IOException {
    }

    public static void main(String[] args) throws Exception{
        new NioServer().start();
    }

    public void start() throws Exception{
        ServerSocketChannel socketChannel=ServerSocketChannel.open();
        socketChannel.socket().bind(new InetSocketAddress("127.0.0.1",9092),1200);
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while(true){
            int length=selector.select();
            if(length>0){
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                for (Iterator<SelectionKey> i = selectedKeys.iterator(); i.hasNext();) {
                    SelectionKey k = i.next();
                    i.remove();
                    if(k.isAcceptable()){
                        handleAccept(k);
                    }else if(k.isReadable()){
                        handleRead(k);
                    }
                }
            }

        }
    }


    private void handleRead(SelectionKey k) throws IOException {
        SocketChannel channel = (SocketChannel) k.attachment();
        try {
            ByteBuffer buf = ByteBuffer.allocate(1024);
            int readBytes = 0;
            int ret = 0;
            while ((ret = channel.read(buf)) > 0) {
                readBytes += ret;
                if (!buf.hasRemaining())
                    break;

            }
            if (readBytes > 0) {
                buf.flip();
                String message = new String(buf.array(), "utf-8");
                System.out.println(message);
            }

            channel.write(buf);
        }finally {
            channel.finishConnect();
            channel.close();
        }

    }

    private void handleAccept(SelectionKey k) throws IOException {
        ServerSocketChannel channel=(ServerSocketChannel)k.channel();
        SocketChannel acceptedSocket = channel.accept();
        acceptedSocket.configureBlocking(false);
        acceptedSocket.register(selector,SelectionKey.OP_READ,acceptedSocket);
    }
}