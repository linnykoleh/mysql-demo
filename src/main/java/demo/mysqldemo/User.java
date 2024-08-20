package demo.mysqldemo;

import lombok.Data;
import java.sql.Date;

@Data
public class User {

    public static final User DEFAULT_USER = new User();

    private String id;
    private String name;
    private String password;
    private Date birthDate;

}
