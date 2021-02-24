package com.caddie.voice.y1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class Y1Application {

    public static void main(String[] args) {
        SpringApplication.run(Y1Application.class, args);
    }

    @PostConstruct
    public void initApplication(){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
