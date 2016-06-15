package service;

import beans.Protocol;
import io.netty.channel.Channel;

/**
 * Created by zyy on 2016/6/12.
 */
public interface Service {

    /**
    * 执行业务操作
    * */
    void execute(Channel ch, Protocol protocol) throws Exception;
}
