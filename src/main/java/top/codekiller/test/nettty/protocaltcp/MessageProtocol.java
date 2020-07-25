package top.codekiller.test.nettty.protocaltcp;

/**
 * @author codekiller
 * @date 2020/7/24 23:56
 * @Description 协议包
 */
public class MessageProtocol {
    private int len;
    private byte[] content;

    public MessageProtocol() {
    }



    public MessageProtocol(byte[] content, int len) {
        this.content=content;
        this.len=len;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
