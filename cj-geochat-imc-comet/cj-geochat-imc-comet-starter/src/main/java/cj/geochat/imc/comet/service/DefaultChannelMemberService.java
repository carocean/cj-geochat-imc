package cj.geochat.imc.comet.service;

import cj.geochat.imc.comet.IChannelMemberService;
import org.springframework.stereotype.Service;

@Service
public class DefaultChannelMemberService implements IChannelMemberService {
//    @Autowired
//    ChannelMemberRepository channelMemberRepository;
//
//    @Override
//    public List<String> searchAllMembers(String channel, int limit, int offset) {
//        Iterable<ChannelMember> iterable = channelMemberRepository.findByChannel(channel, PageRequest.of(offset, limit));
//        List<String> members = new ArrayList<>();
//        iterable.forEach(e -> members.add(e.getMember()));
//        return members;
//    }
//
//    @Override
//    public List<String> searchOnlineMembers(String channel, int limit, int offset) {
//        Iterable<ChannelMember> iterable = channelMemberRepository.findByChannelAndOnline(channel, true, PageRequest.of(offset, limit));
//        List<String> members = new ArrayList<>();
//        iterable.forEach(e -> members.add(e.getMember()));
//        return members;
//    }
//
//    @Override
//    public List<String> searchOfflineMembers(String channel, int limit, int offset) {
//        Iterable<ChannelMember> iterable = channelMemberRepository.findByChannelAndOnline(channel, false, PageRequest.of(offset, limit));
//        List<String> members = new ArrayList<>();
//        iterable.forEach(e -> members.add(e.getMember()));
//        return members;
//    }
//
//    @Override
//    public void updateMemberLineState(String member, boolean online) {
//        int limit = 500;
//        int offset = 0;
//        while (true) {
//            Iterator<ChannelMember> it = channelMemberRepository.findByMember(member, PageRequest.of(offset, limit)).iterator();
//            int readSize = 0;
//            while (it.hasNext()) {
//                var cm = it.next();
//                channelMemberRepository.updateByMember(cm);
//                readSize++;
//            }
//            if (readSize == 0) {
//                break;
//            }
//            offset += readSize;
//        }
//    }
}
