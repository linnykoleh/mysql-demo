package siege.siegedemo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class User {

    public static final User DEFAULT_USER = new User();

    @Id
    private String id;
    private String name;
    private String lastName;

}
