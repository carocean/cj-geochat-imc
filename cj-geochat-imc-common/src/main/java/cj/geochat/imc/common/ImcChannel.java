package cj.geochat.imc.common;

public class ImcChannel {
    String channel;
    String type;

    public ImcChannel() {
    }

    public ImcChannel(String channel, String type) {
        this.channel = channel;
        this.type = type;
    }

    public static ImcChannel parse(String channelSeg) {
        ImcChannel ch = new ImcChannel();
        int pos = channelSeg.indexOf(".");
        if (pos < 0) {
            ch.channel=channelSeg;
            ch.type = "chat";
        }else{
            ch.channel = channelSeg.substring(0, pos);
            ch.type = channelSeg.substring(pos + 1);
        }
        return ch;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ImcChannel{" +
                "channel='" + channel + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
