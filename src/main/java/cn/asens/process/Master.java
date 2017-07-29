package cn.asens.process;

/**
 * accept request and register task to worker
 */
public interface Master extends Runnable{
    void registerTask(WorkerTask workerTask);


}
