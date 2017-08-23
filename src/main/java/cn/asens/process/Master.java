package cn.asens.process;

/**
 * accept request and register task to worker
 */
public interface Master extends Runnable{

    /**
     * bind port and begin to accept request
     */
    void bind(WorkerPool workerPool);

    /**
     * handle the income requests ,accept and register to worker thread
     */
    void handleRequest();
}
