package org.example.movierentals;

import org.example.movierentals.ui.Console;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ClientApp {
    public static void main(String[] args) {
        System.out.println("client...");

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext("org.example.movierentals.config");) {

            Console console = context.getBean(Console.class);
            console.runConsole();
        }
    }
}
