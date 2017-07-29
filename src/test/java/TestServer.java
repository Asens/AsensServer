import cn.asens.Starter;

public class TestServer {

    public static void main(String[] args){

        //构建master线程池
        //构建worker线程池

        //启动master线程池
        //启动worker线程池

        //mater接受请求
        //master把任务注册到worker线程
        //worker线程处理请求,接受后关闭
        Starter starter=new Starter();
        starter.start();

    }

}
