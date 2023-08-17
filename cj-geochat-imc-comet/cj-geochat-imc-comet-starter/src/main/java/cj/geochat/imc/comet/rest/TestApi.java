package cj.geochat.imc.comet.rest;

import cj.geochat.ability.api.annotation.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@Tag(description = "收件", name = "消息收件")
@Slf4j
public class TestApi {
    @PostMapping(path = "/test1")
    @Operation(summary = "test", description = "test1")
    @ApiResult
    @ApiResponses({@ApiResponse(responseCode = "2000", description = "ok")})
    public String test(){
        return "xxx";
    }
}
