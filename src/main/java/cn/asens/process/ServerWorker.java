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

import static cn.asens.process.ServerContext.*;

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

    private final static Queue<WorkerTask> taskQueue=new ConcurrentLinkedQueue<>();

    @Override
    public void registerTask(WorkerTask workerTask) {
        taskQueue.add(workerTask);
        wakeUp();
    }

    @Override
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

    public void wakeUp(){
        selector.wakeup();
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
                    try {
                        handleRead(k);
                    }catch (IOException e){
                        SocketChannel channel = (SocketChannel) k.attachment();
                         channel.close();
                    }
                }
            }
        }
    }

    private void handleRead(SelectionKey k) throws IOException {
        SocketChannel channel = (SocketChannel) k.attachment();

        ByteBuffer buf = ByteBuffer.allocate(1024);
        int readBytes = 0;
        int ret = 0;
        boolean failure = true;
        while ((ret = channel.read(buf)) > 0) {
            readBytes += ret;
            if (!buf.hasRemaining())
                break;
            failure=false;
        }
        if (readBytes > 0) {
            buf.flip();
            fireMessageReceived(buf,channel);
        }

        if (ret < 0|| failure) {
            k.cancel();
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
