package cj.geochat.imc.inbox.rest;

import cj.geochat.ability.api.annotation.ApiResult;
import cj.geochat.imc.common.ImcFrame;
import cj.geochat.imc.inbox.service.IInboxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(description = "收件", name = "消息收件")
@Slf4j
public class InboxApi implements IInboxApi {
    @Autowired
    IInboxService inboxService;

    @PostMapping(path = "/inbox")
    @Operation(summary = "收件", description = "消息收件服务。注意：该方法会将请求的header全部放到消息的请求头中，如只想使用干净的head请使用inboxRaw方法收件")
    @ApiResult
    @ApiResponses({@ApiResponse(responseCode = "2000", description = "ok")})
    @Override
    public void inbox(
            HttpServletRequest request,
            @Parameter(description = "请求行，格式：method path protocol/version") @RequestParam String line,
            @Parameter(description = "请求Host，格式：sender.senderType@channel.channelType:port[recipients~rejects]。注意：不要在请求头中设置Host，虽然Host最终会放到head中，为了清晰使用，请使用Host参数赋值") @RequestParam String host,
            @RequestBody(description = "消息体")
            @org.springframework.web.bind.annotation.RequestBody(required = false)
            String body
    ) {
        Map<String, String> head = new LinkedHashMap<>();
        Enumeration<String> hkeys = request.getHeaderNames();
        while (hkeys.hasMoreElements()) {
            String k = hkeys.nextElement();
            if ("host".equalsIgnoreCase(k)) {
                continue;
            }
            String v = request.getHeader(k);
            head.put(k, v);
        }
        var frame = ImcFrame.create(line, host, head, body);
        inboxService.inbox(frame);
    }

    @PostMapping(path = "/inboxRaw")
    @Operation(summary = "以消息协议文本收件", description = "需符合imc消息文本协议")
    @ApiResult
    @ApiResponses({@ApiResponse(responseCode = "2000", description = "ok")})
    @Override
    public void inboxRaw(
            @RequestBody(description = "消息协议文本", required = true)
            @org.springframework.web.bind.annotation.RequestBody
            String frameRaw
    ) {
        var frame = ImcFrame.fromText(frameRaw);
        inboxService.inbox(frame);
    }
}
