package cn.asens.process;

import cn.asens.componet.SocketChannelWrapper;
import cn.asens.log.Log;
import cn.asens.log.LoggerFactory;

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
 * Created by Asens on 2017/8/22
 */
public class ServerWorker implements Worker{
    private Selector selector;
    private Log log= LoggerFactory.getInstance();

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
                log.debug("processRequest worker begin to read");
                SelectionKey k = i.next();
                i.remove();
                int readyOps = k.readyOps();

                if ((readyOps & SelectionKey.OP_WRITE) != 0) {
                    log.debug("selector is writeAble");
                    SocketChannelWrapper channelWrapper = (SocketChannelWrapper) k.attachment();
                    channelWrapper.flush();
                }

                if ((readyOps & SelectionKey.OP_READ) != 0) {
                    try {
                        handleRead(k);
                    }catch (IOException e){
                        log.error("OP_READ IOException");
                        SocketChannel channel = (SocketChannel) k.attachment();
                         channel.close();
                    }
                }


            }
        }
    }

    private void handleRead(SelectionKey k) throws IOException {
        SocketChannelWrapper channelWrapper = (SocketChannelWrapper) k.attachment();
        channelWrapper.setSelectionKey(k);
        channelWrapper.socketChannel.configureBlocking(false);
        //channel.socket().setSendBufferSize(4*1024*1024);
        ByteBuffer buf = ByteBuffer.allocate(1024);
        int readBytes = 0;
        int ret = 0;
        boolean failure = true;
        while ((ret = channelWrapper.socketChannel.read(buf)) > 0) {
            readBytes += ret;
            if (!buf.hasRemaining())
                break;
            failure=false;
        }
        if (readBytes > 0) {
            buf.flip();
            fireMessageReceived(buf,channelWrapper);
        }

        if (ret < 0|| failure) {
            k.cancel();
            channelWrapper.socketChannel.close();
        }
    }


    private void processTask() {
        for(;;){
            if(taskQueue.isEmpty()){
                break;
            }
            WorkerTask task=taskQueue.poll();
            SocketChannel channel=task.getSocketChannel();
            SocketChannelWrapper wrapper=new SocketChannelWrapper(channel);
            try {
                channel.configureBlocking(false);
                channel.register(selector, SelectionKey.OP_READ,wrapper);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
