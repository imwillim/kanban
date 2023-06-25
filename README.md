# Kanban

## Requirements

For building and running the application you need:

- [Java version 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [SDK Amazon Correcto 17](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html)
- [Maven 3](https://maven.apache.org)

## Technology stack

- JWT

- Spring Data JPA

- Spring Security

- PostgreSQL

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.project.kanban.KanbanApplication` class from your IDE.

Alternatively, you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```