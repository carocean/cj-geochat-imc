package cj.geochat.imc.postbox.rest;

import cj.geochat.ability.api.annotation.ApiResult;
import cj.geochat.imc.common.ImcFrame;
import cj.geochat.imc.postbox.IOfflineFrameService;
import cj.geochat.imc.postbox.api.IOfflineFrameApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/offline")
@Tag(description = "离线消息管理", name = "离线消息管理")
@Slf4j
public class OfflineFrameApi implements IOfflineFrameApi {
    @Autowired
    IOfflineFrameService offlineFrameService;

    @GetMapping(path = "/readAndDelete")
    @Operation(summary = "读取并删除离线消息")
    @ApiResult
    @ApiResponses({@ApiResponse(responseCode = "2000", description = "ok")})
    @Override
    public List<String> readAndDelete(String user, int limit, int offset) {
        return offlineFrameService.readAndDelete(user, limit, offset);
    }
}
