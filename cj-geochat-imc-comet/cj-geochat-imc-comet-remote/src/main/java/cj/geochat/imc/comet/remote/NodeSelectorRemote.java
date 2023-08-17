package cj.geochat.imc.comet.remote;

import cj.geochat.imc.master.INodeSelectorApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "nodeSelectorRemote", value = "cj-geochat-imc-master", url = "${app.test.feign.adapter.docker.imc.master.url:}")

public interface NodeSelectorRemote extends INodeSelectorApi {
    @RequestMapping(value = "/api/v1/selector/selectNode", method = RequestMethod.GET)
    @Override
    String selectNode(@RequestParam String according);
}
