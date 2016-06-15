package beans;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 通信协议
 * Created by zyy on 2016/6/12.
 */
public class Protocol implements Serializable {

    public final static int HEADER_LENGTH = 12;

    private int packageLength;  //包长度
    private int headerLength;   //包头长度
    private short CMD;          //请求命令
    private short version = 1;      //版本号
    private byte[] msg;         //消息

    public int getPackageLength() {
        return packageLength;
    }

    public void setPackageLength(int packageLength) {
        this.packageLength = packageLength;
    }

    public int getHeaderLength() {
        return headerLength;
    }

    public void setHeaderLength(int headerLength) {
        this.headerLength = headerLength;
    }

    public short getCMD() {
        return CMD;
    }

    public void setCMD(short CMD) {
        this.CMD = CMD;
    }

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public byte[] getMsg() {
        return msg;
    }

    public void setMsg(byte[] msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Protocol{" +
                "packageLength=" + packageLength +
                ", headerLength=" + headerLength +
                ", CMD=" + CMD +
                ", version=" + version +
                ", msg=" + new String(msg) +
                '}';
    }
}
