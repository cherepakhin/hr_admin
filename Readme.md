### Заготовка для UI проектов со Spring Boot Web

Java 17:

````shell
export JAVA_HOME=/usr/lib/jvm/java-1.17.0-openjdk-amd64
````

Основная цель __ТОЛЬКО FRONTEND__.

- В качестве template использован __Freemarker__ (__spring-boot-starter-freemarker__).
- Простой CRUD. База данных __H2__.
- Использован __Tailwind__ - CSS-фреймворк для оформления интерфейсов.
- При тестировании использован __AssertJ__.
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
### ModelAndView

В стандартном подходе Spring Boot Web должны возвращаться __ModelAndView__:

````java
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("catalog/products")
public class ProductController {
    
    @GetMapping("list")
    ModelAndView list() {
        return new ModelAndView("catalog/products/list", 
            Map.of("products", this.productRepository.findAll()), 
            HttpStatus.OK);
    }
}
````

В этом проекте возвращаются имена __view__. 

````java
	@RequestMapping(value = "/employees/new", method = RequestMethod.GET)
	public String showCreateForm(Model model) {
		log.info("showCreateForm");
		model.addAttribute("employee", new Employee());
		model.addAttribute("positions", positionRepository.findAll());
		log.info("/employees/new: from page={}", currentIndexPage);
		return "create_employee"; // имя template из src/main/resources/templates
	}
````


При этом подходе есть особенности тестирования. Пример:

````java
    @Test
    public void shouldShowEditFormForExistingEmployee() throws Exception {
        // Given
        Position position = new Position(1L, "Developer");
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", position);
        employee.setId(1L);

        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
        given(positionRepository.findAll()).willReturn(List.of(position));

        // When & Then
        mockMvc.perform(get("/employees/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit_employee"))
                .andExpect(model().attributeExists("employee", "positions"))
                .andExpect(model().attribute("employee", employee));
    }
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

Собранный __FAT__ jar будет в target/hr-admin-0.0.3.jar

Использован __org.springframework.boot:spring-boot-maven-plugin__ .

### Запуск

````shell
 /usr/lib/jvm/java-1.17.0-openjdk-amd64/bin/java -jar target/hr-admin-0.0.3.jar
````

Запуск на другом порту:

````shell
/usr/lib/jvm/java-1.17.0-openjdk-amd64/bin/java -jar target/hr-admin-0.0.3.jar --server.port=8088
````

### Использование

Открыть [https://192.168.1.79:8443/?sortField=lastName&direction=asc&page=1](https://192.168.1.79:8443/?sortField=lastName&direction=asc&page=1)

(см. application.yaml)

Для запуска выполнить на v.perm.ru:

````shell
/usr/lib/jvm/java-17-openjdk-amd64/bin/java -jar ./hr-admin-0.0.3.jar  --server.port=8088
````

Открыть [https://v.perm.ru:8088/(https://v.perm.ru:8088/)
(проверено 29/03/26 на компе Игоря. Открывается с предупреждением о сертификате.)

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

### HTTPS

````shell
keytool -genkeypair -alias tomcat -keyalg RSA -keystore keystore.p12 -storetype PKCS12 -validity 365 -storepass changeit
````
Создастся файл ./keystore.p12 
Сохраните его в src/main/resources:

````shell
cp keystore.p12 src/main/resources
````

Добавьте настройки в application.yaml.
````yaml
# Включить HTTPS
server:
  port: 8443
  ssl:
    key-store: classpath:keystore.p12
    key-store-password: changeit
    key-store-type: PKCS12
    key-alias: tomcat
````
4. Запустите приложение

Теперь доступно по: [https://<IP computer>:8443](https://192.168.1.79:8443)

Если хотите, чтобы HTTP → перенаправлялся на HTTPS, добавьте конфигурацию:
SecurityConfig.java:

````java
package net.guides.springboot2.freemarker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .requiresChannel(channel -> channel
                .requestMatchers(AntPathRequestMatcher.antMatcher("/secure/**"))
                .requiresSecure()
            )
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll()
            )
            .portMapper(mapper -> mapper
                .http(8080).mapsTo(8443)
            );
        return http.build();
    }
}
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

[SVG иконки (пример стрелки вниз)](https://svgicons.com/icon/180/arrow-down)
[SVG иконки (пример стрелки влево)](https://svgicons.com/icon/187/arrow-left)


````text
<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Arrow-down SVG Icon</title><path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 20V4m-7 9l7 7l7-7"/></svg>
````


### TODO

Мелкий шрифт на пагинации на телефоне.

Отступы:
- py-2 = padding 8 px
- px-3 = padding 12 px
- mb-2 = margin buttom 8 px
- w-full - растянуть на всю доступную ширину
- justify-center - динамическое центрирование по вертикали
