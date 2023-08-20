package cj.geochat.imc.comet.remote;

import cj.geochat.imc.postbox.api.IOfflineFrameApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "offlineFrameApi", value = "cj-geochat-imc-postbox", url = "${app.test.feign.adapter.docker.imc.postbox.url:}")
public interface OfflineFrameApi extends IOfflineFrameApi {
    @RequestMapping(value = "/api/v1/offline/readAndDelete", method = RequestMethod.GET)
    @Override
    List<String> readAndDelete(@RequestParam String user, @RequestParam int limit, @RequestParam int offset);
}
