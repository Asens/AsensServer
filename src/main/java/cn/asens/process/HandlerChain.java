package cn.asens.process;

import cn.asens.handler.RequestHandler;

/**
 * Created by Asens on 2017/8/30
 */
public interface HandlerChain {

    boolean hasNext();

    RequestHandler next();

}
