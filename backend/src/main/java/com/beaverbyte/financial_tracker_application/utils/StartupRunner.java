package com.beaverbyte.financial_tracker_application.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // Print Success message when the application has started
        System.out.println("Application launch success.");
    }
}
