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

    public static ImcSender toSender(String appId, String user, String account) {
        String sender = String.format("%s/%s#%s", appId, user, account);
        String type = "principal";
        return new ImcSender(sender, type);
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

    public String getType() {
        return type;
    }

    public String toDescriptor() {
        return String.format("%s.%s", sender, type);
    }

    public boolean isPrincipalType() {
        return "principal".equals(type);
    }

    public String appIdInPrincipal() {
        if (isPrincipalType()) {
            return null;
        }
        int pos = sender.indexOf("/");
        if (pos < 0) {
            return sender;
        }
        String appId = sender.substring(0, pos);
        return appId;
    }

    public String userInPrincipal() {
        if (isPrincipalType()) {
            return null;
        }
        int pos = sender.indexOf("/");
        if (pos < 0) {
            return sender;
        }
        String remaining = sender.substring(pos + 1);
        while (remaining.startsWith("/")) {
            remaining = remaining.substring(1);
        }
        pos = remaining.indexOf("#");
        if (pos < 0) {
            return remaining;
        }
        String user = remaining.substring(0, pos);
        return user;
    }

    public String accountInPrincipal() {
        if (isPrincipalType()) {
            return null;
        }
        int pos = sender.indexOf("/");
        if (pos < 0) {
            return sender;
        }
        String remaining = sender.substring(pos + 1);
        while (remaining.startsWith("/")) {
            remaining = remaining.substring(1);
        }
        pos = remaining.indexOf("#");
        if (pos < 0) {
            return remaining;
        }
        String account = remaining.substring(pos + 1);
        while (account.startsWith("#")) {
            account = account.substring(1);
        }
        return account;
    }

    @Override
    public String toString() {
        return "ImcSender{" +
                "sender='" + sender + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
