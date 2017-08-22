package cn.asens.process;

import cn.asens.Constants;
import cn.asens.exception.BindFailException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Asens
 * create 2017-07-30 8:32
 **/

public class ServerMaster implements Master{

    private volatile Selector selector;


    public ServerMaster(){
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerTask(WorkerTask workerTask) {

    }

    public void bind(WorkerPool workerPool) {
        try {
            ServerSocketChannel socketChannel = ServerSocketChannel.open();
            InetSocketAddress address=new InetSocketAddress(Constants.HOST, Constants.PORT);
            socketChannel.socket().bind(address, Constants.BACK_LOG);
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_ACCEPT,workerPool);
        }catch (Throwable t){
            throw new BindFailException("master bind fail :"+t.getMessage());
        }
    }

    public void handleRequest() {
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        if(selectedKeys==null||selectedKeys.size()==0) return;
        for (Iterator<SelectionKey> i = selectedKeys.iterator(); i.hasNext(); ) {
            System.out.println("accept");
            SelectionKey k = i.next();
            i.remove();
            if (k.isAcceptable()) {
                try {
                    handleAccept(k);
                } catch (IOException e) {
                    e.printStackTrace();
                    close();
                }
            }
        }
    }

    private void close() {

    }


    private void handleAccept(SelectionKey k) throws IOException {
        ServerSocketChannel channel=(ServerSocketChannel)k.channel();
        SocketChannel acceptedSocket = channel.accept();
        WorkerPool workerPool=(WorkerPool)k.attachment();
        Worker worker=workerPool.nextWorker();
        worker.registerTask(new WorkerTask(acceptedSocket));
    }

    public void run() {
        //阻塞,接受请求,注册任务
        for(;;){
            try {
                selector.select(500);
                handleRequest();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
