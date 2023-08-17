package cj.geochat.imc.master.rest;

import cj.geochat.ability.api.annotation.ApiResult;
import cj.geochat.imc.master.INodeEndpointContainer;
import cj.geochat.imc.master.INodeSelectorApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/v1/selector")
@Tag(description = "节点选择器", name = "节点选择器")
@Slf4j
public class NodeSelectorApi implements INodeSelectorApi {
    @Autowired
    INodeEndpointContainer nodeEndpointContainer;

    @GetMapping(path = "/selectNode")
    @Operation(summary = "选择节点", description = "根据指定的描述选择节点，每次相同的描述均会选择同一节点")
    @ApiResult
    @ApiResponses({@ApiResponse(responseCode = "2000", description = "ok")})
    @Override
    public String selectNode(@Parameter(description = "依据", required = true) String according) {
        String hex = DigestUtils.md2Hex(according);
        BigInteger num = new BigInteger(hex, hex.length());
        String selNode = nodeEndpointContainer.selectNodeName(num.intValue());
        return selNode;
    }
}
