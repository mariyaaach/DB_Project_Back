package ru.itpark.mashacursah;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
public class MashaCursahApplication {

    public static void main(String[] args) {
        SpringApplication.run(MashaCursahApplication.class, args);
    }

}
