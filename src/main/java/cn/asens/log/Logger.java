package cn.asens.log;

import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Asens on 2017/8/24.
 */
public class Logger implements Log {
    private final static int level=LEVEL_DEBUG;


    @Override
    public void info(Object message) {
        if(isInfoEnabled()) System.out.println("INFO : "+dateStr()+" > "+message);
    }



    @Override
    public void error(Object message) {
        if(isErrorEnabled()) System.out.println("ERROR : "+dateStr()+" > "+message);
    }

    @Override
    public void debug(Object message) {
        if(isDebugEnabled()) System.out.println("DEBUG : "+dateStr()+" > "+message);
    }

    @Override
    public void warn(Object message) {
        if(isWarnEnabled()) System.out.println("WARN : "+dateStr()+" > "+message);
    }

    @Override
    public void trace(Object message) {
        System.out.println("TRACE : "+dateStr()+" > "+message);
    }

    private String dateStr(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    @Override
    public boolean isDebugEnabled() {
        return level<=LEVEL_DEBUG;
    }

    @Override
    public boolean isInfoEnabled() {
        return level<=LEVEL_INFO;
    }

    @Override
    public boolean isErrorEnabled() {
        return level<=LEVEL_ERROR;
    }

    @Override
    public boolean isWarnEnabled() {
        return level<=LEVEL_WARN;
    }
}
