package cj.geochat.imc.common;

import cj.geochat.ability.util.GeochatRuntimeException;

public class ImcVersion {
    String version;
    String protocol;

    public static ImcVersion parse(String requestline) {
        int pos = requestline.lastIndexOf(" ");
        if (pos < 0) {
            throw new GeochatRuntimeException("4000", "协议格式不正确");
        }
        String pt = requestline.substring(pos + 1);
        pos = pt.indexOf("/");
        if (pos < 0) {
            throw new GeochatRuntimeException("4000", "协议格式不正确");
        }
        String p = pt.substring(0, pos);
        String v = pt.substring(pos + 1);
        ImcVersion version = new ImcVersion();
        version.setVersion(v);
        version.setProtocol(p);
        return version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return "ImcVersion{" +
                "version='" + version + '\'' +
                ", protocol='" + protocol + '\'' +
                '}';
    }
}
