package cn.asens.process;

/**
 * accept request and register task to worker
 */
public interface Master extends Runnable{

    /**
     * after accept new request,give the work to workers
     * @param workerTask give what
     */
    void registerTask(WorkerTask workerTask);

    /**
     * bind port and begin to accept request
     */
    void bind();
}
