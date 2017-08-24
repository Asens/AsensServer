package cn.asens;

import cn.asens.process.MasterPool;
import cn.asens.process.WorkerPool;

import java.net.URL;

public class Starter {

    public void start(){
        WorkerPool workerPool=new WorkerPool();
        MasterPool masterPool=new MasterPool();
        masterPool.bind(workerPool);
    }

    public static void main(String[] args){
        new Starter().start();
    }

}
