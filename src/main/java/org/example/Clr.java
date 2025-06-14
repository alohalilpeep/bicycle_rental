package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Clr implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.printf("Hello and welcome!!");
    }
}