### Заготовка для UI проектов со Spring Boot Web

Java 17:

````shell
export JAVA_HOME=/usr/lib/jvm/java-1.17.0-openjdk-amd64
````

Основная цель __ТОЛЬКО FRONTEND__.

- В качестве template использован Freemarker (__spring-boot-starter-freemarker__).
- Простой CRUD. База данных H2.
- Использован Tailwind - CSS-фреймворк для оформления интерфейсов.
- При тестировании использован AssertJ.
- Использован __GigaChat__.
- тесты в __BDD__ стиле с __Mockito__ в EmployeeControllerTest.java
- __DataJpaTest__ в EmployeeRepositoryTest.java

Основной экран:

![doc/main_screen.png](doc/main_screen.png)

![doc/add_employee.png](doc/add_employee.png)

### Создание maven wrapper

````shell
mvn -N wrapper:wrapper -Dmaven=3.9.9
````

````shell
./mvnw --version
````

````text
Apache Maven 3.9.9 (8e8579a9e76f7d015ee5ec7bfcdc97d260186937)
Maven home: /home/vasi/.m2/wrapper/dists/apache-maven-3.9.9/3477a4f1
Java version: 17.0.17, vendor: Ubuntu, runtime: /usr/lib/jvm/java-17-openjdk-amd64
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "6.14.0-37-generic", arch: "amd64", family: "unix"

````

### Сборка

````shell
./mvnw clean package
````

Собранный __FAT__ jar будет в target/springboot2-freemarker-0.0.1-SNAPSHOT.jar .

Использован __org.springframework.boot:spring-boot-maven-plugin__ .

### Запуск

````shell
 /usr/lib/jvm/java-1.17.0-openjdk-amd64/bin/java -jar target/springboot2-freemarker-0.0.1-SNAPSHOT.jar
````

Запуск на другом порту:

````shell
$ /usr/lib/jvm/java-1.17.0-openjdk-amd64/bin/java -jar target/springboot2-freemarker-0.0.1-SNAPSHOT.jar --server.port=8088
````

### Использование

Открыть [http://127.0.0.1:8088/](http://127.0.0.1:8088/)

(см. application.yaml)

### Разное

SQL запросы логируются. Для этого сделена настройка:

````text
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
````
