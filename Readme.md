### Заготовка для UI проектов со Spring Boot Web

- В качестве template использован Freemarker (__spring-boot-starter-freemarker__).
- Простой CRUD. База данных H2.
- Использован Tailwind - CSS-фреймворк для оформления интерфейсов.
- При тестировании использован AssertJ.
- Файлы из gigachat.

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

Java 17:

````shell
export JAVA_HOME=/usr/lib/jvm/java-1.17.0-openjdk-amd64
````

### Сборка

````shell
./mvnw clean package
````

Собранный __FAT__ jar будет в target/springboot2-freemarker-0.0.1-SNAPSHOT.jar .

Использован org.springframework.boot:spring-boot-maven-plugin .

### Запуск

````shell
 /usr/lib/jvm/java-1.17.0-openjdk-amd64/bin/java -jar target/springboot2-freemarker-0.0.1-SNAPSHOT.jar
````

