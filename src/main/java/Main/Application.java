package Main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Rishat_Valitov on 03.03.17.
 */

@SpringBootApplication(scanBasePackages={"Controllers"})
public class Application {

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (java.lang.ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        SpringApplication.run(Application.class, args);
    }
}
