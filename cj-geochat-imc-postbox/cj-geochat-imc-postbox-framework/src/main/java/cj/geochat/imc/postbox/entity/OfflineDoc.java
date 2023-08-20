package cj.geochat.imc.postbox.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document("offline_frame")
public class OfflineDoc {
    @Id
    @Indexed
    String id;
    @Field
    @Indexed
    String user;
    @Field
    String frameRaw;
}
