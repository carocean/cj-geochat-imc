package cj.geochat.imc.comet;

import cj.geochat.imc.common.ImcFrame;

public interface IOfflineFrameWriter {
    void write(String user, ImcFrame frame);

}
