package cj.geochat.imc.common;

import cj.geochat.ability.util.GeochatRuntimeException;
import cj.geochat.imc.common.util.FrameParser;
import com.google.gson.Gson;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.LinkedHashMap;
import java.util.Map;


public final class ImcFrame implements Externalizable {
    private transient String line;
    private transient Map<String, String> head;
    private transient StringBuffer body;

    private ImcFrame() {
        body = new StringBuffer();
        head = new LinkedHashMap<>();
    }

    public ImcFrame(String line, String host) {
        this();
        FrameParser.verifyRequestLine(line);
        FrameParser.verifyHost(host);
        this.line = line;
        this.head.put("Host", host);
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        throw new IOException("不支持序列化");
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        throw new IOException("不支持反序列化");
    }

    public String method() {
        return FrameParser.parseMethod(line);
    }

    public void requestLine(String method, String url, String version) {
        if (version.indexOf("/") < 0) {
            throw new GeochatRuntimeException("4000", "协议格式不正确");
        }
        line = String.format("%s %s %s", method, url, version);
    }

    public String schema() {
        return version().protocol;
    }

    public ImcVersion version() {
        return FrameParser.parseVersion(line);
    }

    public String uri() {
        return FrameParser.parseUri(line);
    }

    public String url() {
        String url = String.format("%s://%s%s", version().getProtocol(), host(), uri());
        return url;
    }

    public String host() {
        return head.get("Host");
    }

    public String path() {
        String uri = uri();
        int pos = uri.indexOf("?");
        if (pos < 0) {
            return uri;
        }
        return uri.substring(0, pos);
    }

    public String queryString() {
        String uri = uri();
        int pos = uri.indexOf("?");
        if (pos < 0) {
            return "";
        }
        return uri.substring(pos + 1);
    }

    public String parameter(String key) {
        UriComponents uri = UriComponentsBuilder.fromUriString(uri()).build();
        return uri.getQueryParams().get(key).stream().findFirst().orElse(null);
    }

    public String[] enumParameter() {
        UriComponents uri = UriComponentsBuilder.fromUriString(uri()).build();
        return uri.getQueryParams().keySet().toArray(new String[0]);
    }

    public boolean containsParameter(String key) {
        UriComponents uri = UriComponentsBuilder.fromUriString(uri()).build();
        return uri.getQueryParams().containsKey(key);
    }

    public String header(String key) {
        return head.get(key);
    }

    public void header(String key, String value) {
        if ("Host".equalsIgnoreCase(key)) {
            throw new GeochatRuntimeException("5000", "Host不能被修改");
        }
        if (key.indexOf("\n") > -1 || key.indexOf("\r") > -1 || value.indexOf("\r") > -1 || value.indexOf("\n") > -1) {
            throw new GeochatRuntimeException("5000", "头的键和值均不能含有换行符");
        }
        head.put(key, value);
    }

    public boolean containsHeader(String key) {
        return head.containsKey(key);
    }

    public void removeHeader(String key) {
        head.remove(key);
    }

    public String[] enumHeader() {
        return head.keySet().toArray(new String[0]);
    }

    public String port() {
        return FrameParser.parsePort(host());
    }

    public ImcChannel channel() {
        return FrameParser.parseChannel(host());
    }

    public ImcSender sender() {
        return FrameParser.parseSender(host());
    }

    public String recipients() {
        return FrameParser.parseRecipients(host());
    }

    public String[] recipientsToList() {
        String recipients = FrameParser.parseRecipients(host());
        if (ObjectUtils.isEmpty(recipients)) {
            return new String[0];
        }
        return recipients.split(",");
    }

    public String rejects() {
        return FrameParser.parseRejects(host());
    }

    public String[] rejectsToList() {
        String rejects = FrameParser.parseRejects(host());
        if (ObjectUtils.isEmpty(rejects)) {
            return new String[0];
        }
        return rejects.split(",");
    }

    public StringBuffer body() {
        return body;
    }

    public ImcFrame copy() {
        ImcFrame frame = new ImcFrame();
        frame.line = line;
        frame.head.putAll(head);
        frame.body.append(body);
        return frame;
    }

    public String toText() {
        String crlf = "\r\n";
        String sp = " ";
        StringBuffer sb = new StringBuffer();
        sb.append(line);
        sb.append(crlf);
        sb.append(String.format("Host:%s%s", sp, head.get("Host")));
        sb.append(crlf);
        for (var entry : head.entrySet()) {
            if ("Host".equalsIgnoreCase(entry.getKey())) {
                continue;
            }
            sb.append(String.format("%s:%s%s", entry.getKey(), sp, entry.getValue()));
            sb.append(crlf);
        }
        sb.append(crlf);
        sb.append(body);
        return sb.toString();
    }

    public static ImcFrame fromText(String text) {
        int pos = 0;
        int index = 0;
        ImcFrame frame = new ImcFrame();
        String remaining = text;
        String crlf = remaining.indexOf("\r\n") > -1 ? "\r\n" : "\n";
        String sp = " ";
        while ((pos = remaining.indexOf(crlf)) > -1) {
            if (index == 0) {
                frame.line = remaining.substring(0, pos);
                remaining = remaining.substring(pos + crlf.length());
                index++;
                continue;
            }
            if (pos == 0) {//body
                String body = remaining.substring(crlf.length());
                if (StringUtils.hasLength(body)) {
                    frame.body.append(body);
                }
                break;
            }
            String headSegment = remaining.substring(0, pos);
            int hpos = headSegment.indexOf(":");
            String k = headSegment.substring(0, hpos);
            String v = headSegment.substring(hpos + 1 + sp.length());
            if (v == null) {
                v = "";
            }
            frame.head.put(k, v);
            remaining = remaining.substring(pos + crlf.length());
            index++;
        }
        if (frame.head.isEmpty() || !StringUtils.hasLength(frame.line)) {
            throw new GeochatRuntimeException("5000", "无效的侦");
        }
        return frame;
    }

    public byte[] toBytes() {
        return toText().getBytes();
    }

    public static ImcFrame fromBytes(byte[] b) {
        return fromText(new String(b));
    }

    public String toJson() {
        Map<String, Object> frameRaw = new LinkedHashMap<>();
        frameRaw.put("line", line);
        frameRaw.put("head", head);
        frameRaw.put("body", body.toString());
        return new Gson().toJson(frameRaw);
    }

    public static ImcFrame fromJson(String json) {
        Map<String, Object> frameRaw = new Gson().fromJson(json, LinkedHashMap.class);
        ImcFrame frame = new ImcFrame();
        frame.line = (String) frameRaw.get("line");
        frame.head = (Map<String, String>) frameRaw.get("head");
        frame.body.append((String) frameRaw.get("body"));
        if (frame.head.isEmpty() || !StringUtils.hasLength(frame.line)) {
            throw new GeochatRuntimeException("5000", "无效的侦");
        }
        return frame;
    }

    public static ImcFrame create(String line, String host, Map<String, String> head, String body) {
        ImcFrame frame = new ImcFrame(line, host);
        if (head != null && !head.isEmpty()) {
            frame.head.putAll(head);
        }
        if (body != null && body.length() > 0) {
            frame.body.append(body);
        }
        return frame;
    }

    @Override
    public String toString() {
        return toText();
    }
}
