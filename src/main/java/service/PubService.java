package service;

import beans.Protocol;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import util.StringUtil;

/**
 * 发布service
 * Created by zyy on 2016/6/12.
 */
public class PubService implements Service {


    private int CMD = 1;     //命令序号
    private int CMD_REP = 2; //回复


    public void execute(Channel ch, Protocol protocol) throws Exception {
        System.out.println( ServiceApplication.channelGroup.size());
        if (protocol != null) {
            protocol.setCMD((short) 3);
            ServiceApplication.channelGroup.writeAndFlush(protocol);
        }
/*        if (protocol != null) {
            // arr[0] topic ; arr[1] msg;
            String[] arr = new String(protocol.getMsg()).split(StringUtil.getLineDelimiter());
            if (arr == null || arr.length != 2)
                throw new RuntimeException("发布消息参数出现异常！");
            if (ServiceApplication.subTopicMap.containsKey(arr[0])) {
                ChannelGroup channelGroup = ServiceApplication.subTopicMap.get(arr[0]);
                if (channelGroup != null && channelGroup.size() > 0) {
                    Protocol proto = new Protocol();
                    proto.setCMD((short) 3);
                    proto.setMsg(arr[1].getBytes());
                    channelGroup.writeAndFlush(proto); //需要发布的消息
                }
            }
        }*/
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
