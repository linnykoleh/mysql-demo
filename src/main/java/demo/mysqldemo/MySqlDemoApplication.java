package demo.mysqldemo;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@RequiredArgsConstructor
public class MySqlDemoApplication {

    private final MySQLRepository mySQLRepository;

    public static void main(String[] args) {
        SpringApplication.run(MySqlDemoApplication.class, args);
    }

}
