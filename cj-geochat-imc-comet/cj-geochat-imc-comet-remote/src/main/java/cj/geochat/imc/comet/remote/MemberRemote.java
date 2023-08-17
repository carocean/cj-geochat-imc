package cj.geochat.imc.comet.remote;

import cj.geochat.soc.api.IMemberApi;
import cj.geochat.soc.entity.Member;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "memberRemote", value = "cj-geochat-soc", url = "${app.test.feign.adapter.docker.soc.url:}")

public interface MemberRemote extends IMemberApi {
    @RequestMapping(value = "/api/v1/member/listMemberByChannel", method = RequestMethod.GET)
    @Override
    List<Member> listMemberByChannel(@RequestParam String channel, @RequestParam int limit, @RequestParam int offset);
    @RequestMapping(value = "/api/v1/member/listMemberByChannelAndOnline", method = RequestMethod.GET)
    @Override
    List<Member> listMemberByChannelAndOnline(@RequestParam String channel, @RequestParam boolean online, @RequestParam int limit, @RequestParam int offset);
    @RequestMapping(value = "/api/v1/member/updateOnline", method = RequestMethod.GET)
    @Override
    void updateOnline(@RequestParam String member, @RequestParam boolean online);
}
