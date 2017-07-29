package cn.asens.process;

/**
 * @author Asens
 * create 2017-07-29 17:38
 **/

public interface Worker extends Runnable{
    void registerTask(WorkerTask workerTask);
}
