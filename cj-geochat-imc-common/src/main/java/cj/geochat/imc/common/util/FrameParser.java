package cj.geochat.imc.common.util;

import cj.geochat.ability.util.GeochatRuntimeException;
import cj.geochat.imc.common.ImcChannel;
import cj.geochat.imc.common.ImcSender;
import cj.geochat.imc.common.ImcVersion;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

// 请求的url构成是：schema://host/path?queryString三个部分
// 其中schema来自由请求行的protocol，这点与http协议定义不同。
// 其中host是：sender.senderType@channel.channelType:port[recipients~rejects]部分。这与http协议定义不同。
// 规范：schema://sender.senderType@channel.channelType:port[recipients~rejects]/path/?querystring
// imc://cj@iewid8s083kskd.group/test/ab.cc?a=1&b=2&c=3
// imc://iewid8s083kskd.group/test/ab.cc?a=1&b=2&c=3
// imc://cj.user@iewid8s083kskd.group:*/test/ab.cc?a=1&b=2&c=3
// imc://82938923.device@iewid8s083kskd.group:*~/test/ab.cc?a=1&b=2&c=3
// imc://iweisid.session@iewid8s083kskd.group:tom,cat/test/ab.cc?a=1&b=2&c=3
// imc://cj.user@iewid8s083kskd.group:liy,weiz~mike,cat/test/ab.cc?a=1&b=2&c=3
public final class FrameParser {
    public static String parseMethod(String requestline) {
        int pos = requestline.indexOf(" ");
        if (pos < 0) {
            throw new GeochatRuntimeException("4000", "协议格式不正确");
        }
        return requestline.substring(0,pos);
    }

    public static ImcVersion parseVersion(String requestline) {
        return ImcVersion.parse(requestline);
    }

    public static String parseUri(String requestline) {
        int pos = requestline.indexOf(" ");
        if (pos < 0) {
            throw new GeochatRuntimeException("4000", "协议格式不正确");
        }
        String remaining = requestline.substring(pos + 1);
        while (remaining.startsWith(" ")) {
            remaining = remaining.substring(1);
        }
        pos = remaining.indexOf(" ");
        if (pos < 0) {
            throw new GeochatRuntimeException("4000", "协议格式不正确");
        }
        String uri = remaining.substring(0, pos);
        return uri;
    }

    public static void verifyRequestLine(String requestline) {
        String method = parseMethod(requestline);
        if (!StringUtils.hasText(method)) {
            throw new GeochatRuntimeException("4001", "请求行是方法不正确");
        }
        String url = parseUri(requestline);
        if (!StringUtils.hasText(url) || !url.startsWith("/")) {
            throw new GeochatRuntimeException("4001", "请求行中地址不正确");
        }
        ImcVersion version = parseVersion(requestline);
        if (!StringUtils.hasText(version.getVersion()) || !StringUtils.hasText(version.getProtocol())) {
            throw new GeochatRuntimeException("4001", "请求行中协议不正确");
        }
    }

    // sender.senderType@channel.channelType:port[recipients~rejects]
    public static void verifyHost(String host) {
        if (ObjectUtils.isEmpty(parseChannel(host)) || ObjectUtils.isEmpty(parseSender(host))) {
            throw new GeochatRuntimeException("4002", "Host格式不正确");
        }
    }

    // sender.senderType@channel.channelType:port[recipients~rejects]
    public static String parsePort(String host) {
        int pos = host.lastIndexOf(":");
        if (pos < 0) {
            return "";
        }
        String port = host.substring(pos + 1);
        return port;
    }

    // sender.senderType@channel.channelType:port[recipients~rejects]
    public static String parseRecipients(String host) {
        String port = parsePort(host);
        int pos = port.indexOf("~");
        if (pos < 0) {
            return port;
        }
        return port.substring(0, pos);
    }

    // sender.senderType@channel.channelType:port[recipients~rejects]
    public static String parseRejects(String host) {
        String port = parsePort(host);
        int pos = port.indexOf("~");
        if (pos < 0) {
            return "";
        }
        return port.substring(pos + 1);
    }

    // sender.senderType@channel.channelType:port[recipients~rejects]
    public static ImcChannel parseChannel(String host) {

        String remaining = host;
        int pos = remaining.indexOf("@");
        if (pos < 0) {
            pos = remaining.indexOf(":");
            if (pos < 0) {
                return ImcChannel.parse(remaining);
            }
            String channelSeg = remaining.substring(0, pos);
            return ImcChannel.parse(channelSeg);
        }
        remaining = remaining.substring(pos+1);
        pos = remaining.indexOf(":");
        if (pos < 0) {
            return ImcChannel.parse(remaining);
        }
        String channelSeg = remaining.substring(0, pos);
        return ImcChannel.parse(channelSeg);
    }

    // sender.senderType@channel.channelType:port[recipients~rejects]
    public static ImcSender parseSender(String host) {
        int pos = host.indexOf("@");
        if (pos < 0) {
            return new ImcSender("", "");
        }
        String senderSeg = host.substring(0, pos);
        return ImcSender.parse(senderSeg);
    }


}
