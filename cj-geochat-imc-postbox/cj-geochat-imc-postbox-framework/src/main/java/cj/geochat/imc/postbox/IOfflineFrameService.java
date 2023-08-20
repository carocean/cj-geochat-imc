package cj.geochat.imc.postbox;

import cj.geochat.imc.common.ImcFrame;

import java.util.List;

public interface IOfflineFrameService {
    void write(ImcFrame frame);

    List<String> readAndDelete(String user,int limit,int offset);
}
