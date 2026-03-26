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

### Тестирование

````shell
./mvnw clean test
````

Подключен плагин JaCoCo report для создания отчета покрытия тестами.

Для генерации отчета выполнить:

````shell
./mvnw jacoco:report
````

Отчет в [target/site/jacoco/index.html](target/site/jacoco/index.html).

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

### Размещение на tomcat сервере

Сборка:

````shell
./mvnw clean package -DskipTests
````

Полученный файл target//springboot2-freemarker-0.0.2.war задеплоить в Tomcat через панель управления Tomcat.

![tomcat.png](doc/tomcat.png)

Доступ [http://v.perm.ru:8088/](http://v.perm.ru:8088/)

не HTTPS!

### Разное

SQL запросы логируются. Для этого сделена настройка:

````text
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
````

Не переносить слова __whitespace-nowrap__: 

````text
<span class="ml-3 whitespace-nowrap">Скрыть панель</span>
````

В контроллерах использовать __RequestMapping__ вместо __GetMapping__. Почему? 
Потому что здесь не простой REST Controller, а управление страницами. Методы те же, но нужна дополнительная функциональность типа "redirect". 

### О Freemarker

[https://habr.com/ru/articles/420549/](https://habr.com/ru/articles/420549/)

#### Пример 1:

````html
<ul>
  <#list father as item>
      <li>${item}</li>
  </#list>
</ul>
````

````java
Map<String, Object> root = new HashMap<>();
....
root.put("father", Arrays.asList("Alexander", "Petrov", 47));
````


#### Пример 2:

Шаблон hello_test.ftl:

````html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Hello ${name}!</title>
</head>
<body>

<input type="text" placeholder="${name}">

<table>
    <#list persons as row>
    <tr>
        <#list row as field>
            <td>${field}</td>
        </#list>
    </tr>
    </#list>
</table>

</body>
</html>

````

````java
@Component
public class CommandLine implements CommandLineRunner {

    @Autowired
    private Configuration configuration;

    public void run(String... args) {
        Map<String, Object> root = new HashMap<>();
        // для ${name}
        root.put("name", "Fremarker");
        // для <#list persons
        List<List> persons = new ArrayList<>();
        persons.add(Arrays.asList("Alexander", "Petrov", 47));
        persons.add(Arrays.asList("Slava", "Petrov", 13));
        root.put("persons", persons);

        try {
            Template template = configuration.getTemplate("hello_test.ftl");
            Writer out = new OutputStreamWriter(System.out);
            try {
                template.process(root, out);
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
````

Результат:

````html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Hello Fremarker!</title>
</head>
<body>

<input type="text" placeholder="Fremarker">

<table>
    <tr>
            <td>Alexander</td>
            <td>Petrov</td>
            <td>47</td>
    </tr>
    <tr>
            <td>Slava</td>
            <td>Petrov</td>
            <td>13</td>
    </tr>
</table>
</body>
````

### Макросы:

#### Пример 1
Объявление макроса __textInput__ в файле __"ui.ftl"__:

````html
<#macro textInput id value="-">
  <input type="text" id="${id}" value="${value}">
</#macro>
````
Ключевое слово __<#macro>__ , __</#macro>__.
__textInput__ имя макроса.
__id__ и __value__ - параметры макроса (value="-" - значение по умолчанию).

Подключение макроса через __import__:
(ключевое слово __#import__)

````html
<#import "ui.ftl" as ui/>
````
"ui" - алиас для использования.

В шаблоне макрос "textInput" из импорта "ui" вызывается так:

````html
<@ui.textInput id="name" value="${name}"/>
````

### Пример 2:

Файл макрос__ОВ__ __ui.ftl__:

````html
<#-- textInput macro for html input -->
<#macro textInput id placeholder="" value="">
  <input type="text" id="${id}" placeholder="${placeholder}" value="${value}">
</#macro>

<#-- table macro for html table -->
<#macro table id rows>
<table id="${id}">
    <#list rows as row>            <#-- ЦИКЛ!!! -->                   
    <tr>
        <td>${row?index + 1}</td>  <#-- ВЫЧИСЛЕНИЕ!!! -->
        <#list row as field>       <#-- ЦИКЛ!!! -->
            <td>${field}</td>
        </#list>
    </tr>
    </#list>
</table>
</#macro>
````

Использование __ui.ftl__ в __hello.ftl__:

````html
<#import "ui.ftl" as ui/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Hello ${name}!</title>
</head>
<body>

<@ui.textInput id="name" placeholder="Enter name" value="${name}"/>
<@ui.table id="table1" rows=persons/>

</body>
</html>
````

### Ссылки

[FreeMarker шаблоны (habr)](https://habr.com/ru/articles/420549/)
[Альтернатива Freemarker - Velocity](https://velocity.apache.org/).

### Примечания

<div class="rounded-md"> - скругленный углы
class="flex-1" - класс в Tailwind CSS, который позволяет элементу занимать равную долю доступного пространства в контейнере Flexbox.

При тестировании с mock, если используется any...(), то остальные поля должны быть заданы в виде типа eq(...)
Пример:

````java
verify(this.employeeRepository, times(1)).findByFiltersAndSort(eq("firstName1"), eq("lastName1"), eq("email1"), any());
````


### TODO

Мелкий шрифт на пагинации на телефоне.

Отступы:
- py-2 = padding 8 px
- px-3 = padding 12 px
- mb-2 = margin buttom 8 px
- w-full - растянуть на всю доступную ширину
- justify-center - динамическое центрирование по вертикали
