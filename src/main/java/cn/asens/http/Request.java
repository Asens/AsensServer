package cn.asens.http;

/**
 * Created by Asens on 2017/8/24.
 */
public interface Request {
    String getMethod();

    String getRequestPath();

    String getHost();
}
