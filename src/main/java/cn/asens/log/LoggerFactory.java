package cn.asens.log;

/**
 * Created by Asens on 2017/8/24.
 */
public class LoggerFactory {
    public static Logger logger=new Logger();

    public static Log getInstance(){
        return logger;
    }
}
