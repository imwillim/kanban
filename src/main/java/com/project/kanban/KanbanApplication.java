package com.project.kanban;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// @SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@SpringBootApplication
public class KanbanApplication {
    public static void main(String[] args) {
        SpringApplication.run(KanbanApplication.class, args);
    }
}
