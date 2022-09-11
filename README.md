   Restaurants 
   =
   ### Java Enterprise Online Project

This is a graduation project for the [Topjava](https://topjava.ru/topjava) and [BootJava](https://javaops.ru/view/bootjava) courses.
The task for graduation project was:

---------------------------------------------------------------------------------------------------
Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) without frontend.
Build a voting system for deciding where to have lunch.

- 2 types of users: admin and regular users
- Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
- Menu changes each day (admins do the updates)
- Users can vote on which restaurant they want to have lunch at
- Only one vote counted per user
- If user votes again the same day:
   1. If it is before 11:00 we assume that he changed his mind.
   2. If it is after 11:00 then it is too late, vote can't be changed
Each restaurant provides a new menu each day.
  
---------------------------------------------------------------------------------------------------
The most popular technologies/ tools/ frameworks used in project:

**[JDK 17](http://jdk.java.net/17/) /Maven/ Spring/[Spring Security](http://projects.spring.io/spring-security/) /[Spring Data JPA](http://projects.spring.io/spring-data-jpa/) /[Spring Security Test](http://spring.io/blog/2014/05/07/preview-spring-security-test-method-security) /[Hibernate ORM](http://hibernate.org/orm/) /[Hibernate Validator](http://hibernate.org/validator/) /[SLF4J](http://www.slf4j.org/) /[Json Jackson](https://github.com/FasterXML/jackson) /[JUnit 5](https://junit.org/junit5/) /[Hamcrest](http://hamcrest.org/JavaHamcrest/) /[AssertJ](https://assertj.github.io/doc/) /REST(Jackson)  
Spring Boot 2.5/ Lombok/ H2/ Caffeine Cache/ Swagger/OpenAPI 3.0.**

- Run: `mvn spring-boot:run` in root directory. Then you can start:
[SWAGGER REST API documentation](http://localhost:8080/swagger-ui.html) with
test credentials:
```
Admin: admin@gmail.com / admin
User:  user@yandex.ru / password
Guest: guest@gmail.com / guest
```