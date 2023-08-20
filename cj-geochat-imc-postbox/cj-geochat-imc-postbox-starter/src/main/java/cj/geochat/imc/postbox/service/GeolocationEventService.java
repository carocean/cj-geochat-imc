package cj.geochat.imc.postbox.service;

import cj.geochat.imc.common.ImcFrame;
import cj.geochat.imc.postbox.IGeolocationEventService;
import cj.geochat.imc.postbox.entity.UserLocationDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class GeolocationEventService implements IGeolocationEventService {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void update(ImcFrame frame) {
        String user = frame.sender().tryGetUser();
        if (!StringUtils.hasLength(user)) {
            user = frame.header("Geo-User");
        }
        String longitude = frame.parameter("longitude");
        String latitude = frame.parameter("latitude");
        String address = frame.body().toString();
        long utime = System.currentTimeMillis();
        UserLocationDoc doc = new UserLocationDoc();
        doc.setUser(user);
        doc.setLocation(new GeoJsonPoint(Double.valueOf(longitude), Double.valueOf(latitude)));
        doc.setUtime(utime);
        doc.setAddress(address);
        mongoTemplate.save(doc);
    }
}
