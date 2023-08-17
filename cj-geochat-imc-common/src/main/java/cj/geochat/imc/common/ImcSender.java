package cj.geochat.imc.common;

public class ImcSender {
    String sender;
    String type;

    public ImcSender() {
    }

    public ImcSender(String sender, String type) {
        this.sender = sender;
        this.type = type;
    }

    public static ImcSender parse(String senderSeg) {
        int pos = senderSeg.indexOf(".");
        if (pos < 0) {
            return new ImcSender(senderSeg, "user");
        }
        var sender = new ImcSender();
        sender.sender = senderSeg.substring(0, pos);
        sender.type = senderSeg.substring(pos + 1);
        return sender;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ImcSender{" +
                "sender='" + sender + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
