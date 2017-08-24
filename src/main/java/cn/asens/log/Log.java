package cn.asens.log;

/**
 * Created by Asens on 2017/8/24.
 */
public interface Log {
    public final static int LEVEL_DEBUG=1;
    public final static int LEVEL_INFO=2;
    public final static int LEVEL_WARN=3;
    public final static int LEVEL_ERROR=4;

    void info(Object message);

    void error(Object message);

    void debug(Object message);

    void warn(Object message);

    void trace(Object message);

    boolean isDebugEnabled();

    boolean isInfoEnabled();

    boolean isErrorEnabled();

    boolean isWarnEnabled();


}
