package cn.asens.process;

import java.nio.channels.SocketChannel;

public class WorkerTask {
    private SocketChannel socketChannel;

    public WorkerTask(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }
}
