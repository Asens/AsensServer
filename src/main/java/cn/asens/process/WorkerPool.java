package cn.asens.process;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Asens
 * create 2017-07-29 17:36
 **/

public class WorkerPool {
    private final static int POOL_SIZE=Runtime.getRuntime().availableProcessors();
    private AtomicInteger index=new AtomicInteger(0);
    private Worker[] workers;
    private final static ExecutorService pool= Executors.newFixedThreadPool(POOL_SIZE);
    private final static String WORKER_PREFIX="server_worker_";

    public WorkerPool() {
        init();
    }

    private void init() {
        workers=new Worker[POOL_SIZE];
        for(int i=0;i<workers.length;i++){
            Worker worker=new ServerWorker();
            pool.execute(new Thread(worker,WORKER_PREFIX+i));
            workers[i]=worker;
        }
    }

    public Worker nextWorker() {
        return workers[index.getAndIncrement()%POOL_SIZE];
    }
}
