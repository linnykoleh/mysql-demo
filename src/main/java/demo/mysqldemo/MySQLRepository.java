package demo.mysqldemo;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class MySQLRepository {

    private static final String INSERT_USER = "INSERT INTO users (username, password, birthdate) VALUES (?, ?, ?)";

    @Value("${mysql.url}")
    private String dataSourceUrl;

    @Value("${mysql.username}")
    private String username;

    @Value("${mysql.password}")
    private String password;

    @Value("${mysql.driver-class-name}")
    private String driver;

    @Value("${mysql.pool.size}")
    private int maxPoolSize;

    private DataSource dataSource = null;

    @PostConstruct
    private void setUp() {
        try {
            var cpds = new ComboPooledDataSource();
            cpds.setDriverClass(driver); //loads the jdbc driver
            cpds.setJdbcUrl(dataSourceUrl);
            cpds.setUser(username);
            cpds.setPassword(password);
            cpds.setPreferredTestQuery("SELECT 1");
            cpds.setTestConnectionOnCheckout(true);
            cpds.setMaxPoolSize(maxPoolSize);
            dataSource = cpds;
        } catch (Exception e) {
            dataSource = null;
        }
    }

    public void insertUser(User user) {
        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(INSERT_USER)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setDate(3, user.getBirthDate());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

}
