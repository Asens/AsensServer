package cn.asens.process;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Asens on 2017/8/22.
 */
public class ServerWorker implements Worker{
    private Selector selector;

    public ServerWorker() {
        try {
            selector=Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final static Queue<WorkerTask> taskQueue=new ConcurrentLinkedQueue<WorkerTask>();

    public void registerTask(WorkerTask workerTask) {
        taskQueue.add(workerTask);
    }

    public void run() {
        for (; ; ) {
            try {
                selector.select(500);
                processTask();
                processRequest();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processRequest() throws IOException {
        while(true){
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            if(selectedKeys.isEmpty()) return;
            for (Iterator<SelectionKey> i = selectedKeys.iterator(); i.hasNext(); ) {
                System.out.println("read");
                SelectionKey k = i.next();
                i.remove();
                if (k.isReadable()) {
                    handleRead(k);
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

    private void processTask() {
        for(;;){
            if(taskQueue.isEmpty()){
                break;
            }
            WorkerTask task=taskQueue.poll();
            SocketChannel channel=task.getSocketChannel();
            try {
                channel.configureBlocking(false);
                channel.register(selector, SelectionKey.OP_READ,channel);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
