package service;

import beans.Protocol;
import io.netty.channel.Channel;

/**
 * Created by zyy on 2016/6/16.
 */
public class HeatResponseService implements  Service {

    private int CMD = 5;     //命令序号
    private int CMD_REP = 6; //回复

    public void execute(Channel ch, Protocol protocol) throws Exception {
        protocol.setCMD((short) this.CMD_REP);
        System.out.println("收到客户端" + ch.remoteAddress().toString() + " 发来一次心跳包");
        ch.writeAndFlush(protocol);
    }

    public int getCMD() {
        return CMD;
    }

    public void setCMD(int CMD) {
        this.CMD = CMD;
    }

    public int getCMD_REP() {
        return CMD_REP;
    }

    public void setCMD_REP(int CMD_REP) {
        this.CMD_REP = CMD_REP;
    }
}
