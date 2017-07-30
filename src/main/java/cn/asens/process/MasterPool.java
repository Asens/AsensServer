package cn.asens.process;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * MasterPool
 *
 * @author Asens
 * create 2017-07-29 17:35
 **/

public class MasterPool {
    private Master[] masters;
    private AtomicInteger masterIndex=new AtomicInteger(0);
    private final static int POOL_SIZE=1;
    private final static String MASTER_PREFIX="server_master_";

    public MasterPool(){
        this(POOL_SIZE);
    }

    public MasterPool(int threadSize){
        masters=new Master[threadSize];
        init();
    }

    private void init() {
        int threadSize=masters.length;
        ExecutorService es= Executors.newFixedThreadPool(threadSize);
        for(int i=0;i<threadSize;i++){
            Master master=new ServerMaster();
            masters[i]=master;
            Thread thread=new Thread(master);
            thread.setName(MASTER_PREFIX+i);
            es.execute(thread);
        }
    }

}
