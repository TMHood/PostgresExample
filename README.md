# PostgresExample
This is a sample Spring Boot application to demonstrate how esure applications
communicate with Postgres.
It is designed to be as simple as possible, to illustrate the basic principles without distracting details.
The application simply inserts three records into a table called UCD.PROPERTIES in local Postgres database UCD.

# How This Project Was Created

## 1. Initializr
select the following dependencies:
- Spring Boot Dev Tools
- Lombok
- Spring Data JPA
- PostgresQL Driver
options:
- Java 11

## 2. Set up an Empty Postgres Database
in server/database localhost/local:
```
    create user ucd;
    alter user ucd with password 'ucd';
    create database springbootjpa;
    grant all privileges on database ucd to ucd;
    
    create user ucd_user;
    alter user ucd_user with password 'ucd_user';
    grant usage on schema ucd to ucd_user;
```

## 3. application.properties
create application.properties in resources folder, as follows:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/ucd
spring.datasource.username=ucd_user
spring.datasource.password=ucd_user
spring.jpa.properties.hibernate.default_schema=ucd
```
application can now be run (though it does nothing)

## 4. Create a Postgres Table
Within Pgadmin:
```
create database ucd
```
then attach to database ucd, then:
```
create schema ucd;
create table ucd.properties
(
  id          bigserial    primary key,
  short_name  varchar(30)  not null,
  long_name   varchar(30)
);
create unique index properties_ix on ucd.properties (short_name);
grant usage on schema ucd to ucd_user;
grant select, insert, delete on ucd.properties to ucd_user;
grant usage on ucd.properties_id_seq TO ucd_user;
```
## 5. Create Entity
```java
package sparrows.ucd.entities;
@Entity
@Table(name = "properties")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode()
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PropertyEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",insertable = false)
    @Id
    private Long id;

    @NotNull
    @Column(name = "short_name")
    private String shortName;

    @NotNull
    @Column(name = "long_name")
    private String longName;
}
```
## 6. Create a Repository
```java
package sparrows.ucd.repositories;
@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, Long> {
}
```
The Postgres interface is now ready. Normally, this would be used by an HTTP call or
from a queue listener. However, for this example, we'll create a very simple service
to insert some rows into the table, and call the service directly from the main program.

## 7. Create a Service to Test the Repository
```java
package sparrows.ucd.services;

@Service
@RequiredArgsConstructor
public class TestService {
    private final PropertyRepository repository;

    public void Insert() {
        repository.deleteAll();

        PropertyEntity property = new PropertyEntity();
        property.setId(null);
        property.setShortName("sh");
        property.setLongName("long");
        repository.save(property);
        property.setId(null);
        property.setShortName("sh2");
        property.setLongName("long2");
        repository.save(property);
        property.setId(null);
        property.setShortName("sh3");
        property.setLongName("long3");
        repository.save(property);
    }
}
```
## 8. Modify the Main Class to Call the Service then Exit
```java
@SpringBootApplication
public class UcdApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(UcdApplication.class, args);
		System.out.println("now I'm here");

		TestService service = context.getBean(TestService.class);
		service.Insert();

		System.out.println("now I'm there");

		SpringApplication.exit(context, () -> 0);
	}

}
```