package cj.geochat.imc.postbox.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document("user_location")
public class UserLocationDoc {
    @Id
    @Indexed
    String id;
    @Field
    @Indexed
    String user;
    @Field
    @Indexed
    GeoJsonPoint location;
    @Field
    String address;
    @Field
    @Indexed
    long utime;
}
