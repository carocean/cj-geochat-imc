package cj.geochat.imc.postbox.service;

import cj.geochat.imc.common.ImcFrame;
import cj.geochat.imc.postbox.IOfflineFrameService;
import cj.geochat.imc.postbox.entity.OfflineDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class OfflineFrameService implements IOfflineFrameService {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void write(ImcFrame frame) {
        String user = frame.header("Offline-User");
        if (!StringUtils.hasLength(user)) {
            return;
        }
        OfflineDoc doc = new OfflineDoc();
        doc.setUser(user);
        doc.setFrameRaw(frame.toText());
        mongoTemplate.save(doc);
    }

    @Override
    public List<String> readAndDelete(String user, int limit, int offset) {
        Query query = Query.query(Criteria.where("user").is(user));
        query.limit(limit);
        query.skip(offset);
        List<OfflineDoc> docs = mongoTemplate.findAllAndRemove(query, OfflineDoc.class);
        List<String> frames = new ArrayList<>();
        for (OfflineDoc doc : docs) {
            frames.add(doc.getFrameRaw());
        }
        return frames;
    }
}
