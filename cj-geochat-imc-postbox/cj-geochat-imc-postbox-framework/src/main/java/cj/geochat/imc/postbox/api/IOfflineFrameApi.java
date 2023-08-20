package cj.geochat.imc.postbox.api;

import java.util.List;

public interface IOfflineFrameApi {
    List<String> readAndDelete(String user, int limit, int offset);
}
