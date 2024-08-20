package demo.mysqldemo;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class UserService {

    private final MySQLRepository mySQLRepository;

    private final SimpleDateFormat originalFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
    private final SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

    @EventListener(ApplicationReadyEvent.class)
    private void start() throws Exception {
//        createUsers();
        generateUsers();
    }

    private void generateUsers() throws Exception {
//        for 30 seconds save users to database
        var faker = new Faker();

        var dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        var startDate = dateFormat.parse("1980-01-01");
        var endDate = dateFormat.parse("2000-01-01");

        var startMillis = startDate.getTime();
        var endMillis = endDate.getTime();

        var start = System.currentTimeMillis();
        var count = 0;
        while (System.currentTimeMillis() - start < 30_000) {
            save(faker, startMillis, endMillis);
            count++;
        }
        System.out.println("Time taken: " + (System.currentTimeMillis() - start) / 1000 + " seconds to create " + count + " users");
    }

    private void createUsers() throws Exception {
        var faker = new Faker();
        var start = System.currentTimeMillis();

        var dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        var startDate = dateFormat.parse("1980-01-01");
        var endDate = dateFormat.parse("2000-01-01");

        var startMillis = startDate.getTime();
        var endMillis = endDate.getTime();

        var pool = Executors.newFixedThreadPool(10);
        for (var i = 0; i < 10; i++) {
            pool.execute(() -> saveUser(faker, startMillis, endMillis));
        }
        pool.shutdown();
        while (!pool.isTerminated()) {
            Thread.sleep(1000);
        }
        System.out.println("Time taken: " + (System.currentTimeMillis() - start) / 1000 + " seconds to create 40 million users");
    }

    private void saveUser(Faker faker, long startMillis, long endMillis) {
        for (var i = 1; i <= 4_000_000; i++) {
            save(faker, startMillis, endMillis);
        }
    }

    private void save(Faker faker, long startMillis, long endMillis) {
        var user = new User();
        user.setName(faker.name().firstName());
        user.setPassword(faker.name().lastName());

        user.setBirthDate(new Date(ThreadLocalRandom.current().nextLong(startMillis, endMillis)));
        mySQLRepository.insertUser(user);
        System.out.println("User: " + user + " created");
    }

}
