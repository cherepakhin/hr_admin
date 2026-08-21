### Заготовка для UI проектов со Spring Boot Web и FreeMarker

Git репозиторий [https://github.com/cherepakhin/hr_admin.git](https://github.com/cherepakhin/hr_admin.git).

Java 17:

````shell
export JAVA_HOME=/usr/lib/jvm/java-1.17.0-openjdk-amd64
````

Открыть [http://127.0.0.1:8088/hr_admin/](http://127.0.0.1:8088/hr_admin/)

URL для разработки [http://127.0.0.1:8088/hr_admin/employees/](http://127.0.0.1:8088/hr_admin/employees/)

Развернуто на [https://v.perm.ru/hr_admin/](https://v.perm.ru/hr_admin/).

Основная цель __ТОЛЬКО FRONTEND__.

- В качестве template использован __Freemarker__ (__spring-boot-starter-freemarker__).
- Простой CRUD. База данных __H2__.
- Использован __Tailwind__ - CSS-фреймворк для оформления интерфейсов.
- При тестировании использован __AssertJ__.
- Использован __GigaChat__.
- тесты в __BDD__ стиле с __Mockito__ в EmployeeControllerTest.java
- __DataJpaTest__ в EmployeeRepositoryTest.java

Основной экран в виде карточек:

![doc/hr_admin.png](doc/hr_admin.png)

Список сотрудников в виде таблицы с сортировкой и СВЕРНУТОЙ панелью:

![doc/list_employees.png](doc/list_employees.png)

Список сотрудников в виде таблицы с сортировкой и РАЗВЕРНУТОЙ панелью:

![doc/show_panel.png](doc/show_panel.png)

Диалог ввода сотрудника:

![doc/add_employee.png](doc/add_employee.png)

Диалог изменение данных сотрудника:

![doc/edit_employee.png](doc/edit_employee.png)

Список должностей:

![doc/positions.png](doc/positions.png)

Редатирование должности:

![doc/edit_position.png](doc/edit_position.png)

Экранные формы для мобильных устройств: [doc/mobile_screens/](doc/mobile_screens/)

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

Внедрение значений в html файлы осуществляется через __Model__ [EmployeeController.java](src/main/java/ru/perm/v/hr_admin/controller/EmployeeController.java) и 
возврат имени view (не ModelAndVew) 

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

Пример использования __positions__ из примера выше в форме [src/main/resources/templates/create_employee.ftlh](src/main/resources/templates/create_employee.ftlh):

````html
                    <!-- Position -->
                    <div>
                        <label for="position_id">Должность</label>
                        <select
                            id="position_id"
                            name="position.id"
                            class="border border-gray-300 shadow-sm py-2 px-3 focus:ring-0 focus:ring-sky-500 focus:outline-none combo-btn"
                            style="width: 12em; padding-left: 1em; padding-right: 1em;"
                            required>
                            <#list positions as pos> <!-- Пример использования positions -->
                                <option value="${pos.id}">${pos.name}</option>
                            </#list>
                        </select>
                    </div>

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

Обращение из __файлов шаблонов (*.ftlh)__ к ресурсам __src/resource/STATIC/__ происходит через переменную __springMacroRequestContext.contextPath__.
Нужно для минимизации запросов к внешним ресурсам интернета (и прочим вопросам безопаснотси). 
Пример из [src/resource/templates/positions.ftlh](src/resource/templates/positions.ftlh):

````
<head>
    <!-- Tailwind CSS via CDN -->
    <script src="${springMacroRequestContext.contextPath}/js/tailwind.js"></script>

    <!-- Alpine.js for interactivity -->
    <!--<script src="https://unpkg.com/alpinejs@3.x.x/dist/cdn.min.js" defer></script>-->
    <script src="${springMacroRequestContext.contextPath}/js/cdn.min.js" defer></script>
...
````

### Тестирование

````shell
export JAVA_HOME=/usr/lib/jvm/java-1.17.0-openjdk-amd64
./mvnw clean test
````

При тестировании с MockMVC, подстановка bean в модель страницы можно выполнить вручную с помощью __.flashAttr__:

````java
		ResultActions result = mockMvc.perform(post("/employees/")
				.flashAttr("positions", asList(position1)) // подстановка в модель аттрибута positions как asList(position1) !!!
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("firstName", "0123456789_0123456789_0123456789_0123456789")  // > 15 символов
				.param("lastName", "LastName")
				.param("email", "user@example.com")
				.param("position.id", "1"));
````

### Покрытие тестами

Подключен плагин JaCoCo report для создания отчета покрытия тестами.

Для генерации отчета выполнить (__ОБЯЗАТЕЛЬНО__ проверить версию Java! export JAVA_HOME=/usr/lib/jvm/java-1.17.0-openjdk-amd64 ):

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

Так же есть [Jenkinsfile](Jenkinsfile)

### Запуск

````shell
 /usr/lib/jvm/java-1.17.0-openjdk-amd64/bin/java -jar target/hr-admin-0.0.3.jar
````

Запуск на другом порту:

````shell
/usr/lib/jvm/java-1.17.0-openjdk-amd64/bin/java -jar target/hr-admin-0.0.3.jar --server.port=8088
````

### Использование

Открыть [http://127.0.0.1:8088/hr_admin/employees/](http://127.0.0.1:8088/hr_admin/employees/)

(см. application.yaml)

Для запуска выполнить на v.perm.ru:

````shell
/usr/lib/jvm/java-17-openjdk-amd64/bin/java -jar ./hr-admin-0.0.3.jar  --server.port=8088
````

Открыть [https://v.perm.ru:8088/](https://v.perm.ru:8088/)
(проверено 29/03/26 на другом компьютере. Открывается с предупреждением о сертификате.)

Ниже более лучший вариант без заморочек с сертификатами.

### Проброс через Apache HTTPS

Размещено на [https://v.perm.ru/hr_admin/](https://v.perm.ru/hr_admin/)

Работа через HTTPS сделана с помощью проксирования через apache2. В приложении никаких настроек на HTTPS __нет__.

В [doc/https/](doc/https/) другая (неудавшаяся) настройка на HTTPS. 

Через __Apache proxy__ настройка проще. Примеры смотри в [doc/https/000-default-le-ssl.conf](doc/https/000-default-le-ssl.conf)

Настройка apache2 (см. # Проксирование запросов для Spring Boot приложения hr_admin (работает по HTTP на localhost:8088)):

````text
root@v:/etc/apache2/sites-enabled# cat 000-default-le-ssl.conf 
<IfModule mod_ssl.c>
    <VirtualHost *:443>
	ServerName v.perm.ru

	ServerAdmin vasi.che@gmail.com
	DocumentRoot /var/www/main/

	LogLevel debug

	ErrorLog ${APACHE_LOG_DIR}/error.log
	CustomLog ${APACHE_LOG_DIR}/access.log combined

    # Настройка HTTPS для Apache2
	SSLEngine on
	
	SSLCertificateFile /etc/letsencrypt/live/v.perm.ru/cert.pem
	SSLCertificateChainFile /etc/letsencrypt/live/v.perm.ru/fullchain.pem
	SSLCertificateKeyFile /etc/letsencrypt/live/v.perm.ru/privkey.pem
	SSLCACertificateFile /etc/letsencrypt/live/v.perm.ru/chain.pem
	Include /etc/letsencrypt/options-ssl-apache.conf

	# Проксирование запросов для Spring Boot приложения hr_admin (работает по HTTP на localhost:8088)
	ProxyPreserveHost On
	ProxyPass /hr_admin http://192.168.1.20:8088/hr_admin
	ProxyPassReverse /hr_admin http://192.168.1.20:8088/hr_admin
	# Передача информации о протоколе (важно для Spring Boot)
	RequestHeader set X-Forwarded-Proto https

	# Дополнительные настройки безопасности и логирования
	Protocols h2 http/1.1
	SSLProtocol all -SSLv3 -TLSv1 -TLSv1.1
	ErrorLog ${APACHE_LOG_DIR}/spring-proxy-error.log
	CustomLog ${APACHE_LOG_DIR}/spring-proxy.log combined
    </VirtualHost>
</IfModule>
````

Логи на сервере смотреть в v.perm.ru:/home/vasi/temp/hr_admin/hr_admin.log.

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

Расширения файлов freemarket должны быть __ftlh__. С расширением .ftlh автоматически активируется защита от XSS-атаксов (HTML-автоэкранирование).

### HTTPS

````shell
keytool -genkeypair -alias tomcat -keyalg RSA -keystore keystore.p12 -storetype PKCS12 -validity 365 -storepass changeit
````

````shell
$ keytool -genkeypair -alias tomcat -keyalg RSA -keystore keystore.p12 -storetype PKCS12 -validity 365 -storepass changeit
Enter the distinguished name. Provide a single dot (.) to leave a sub-component empty or press ENTER to use the default value in braces.
What is your first and last name?
[Unknown]:  Cherepakhin
What is the name of your organizational unit?
[Unknown]:  Vasili OU       
What is the name of your organization?
[Unknown]:  Vasili Organization
What is the name of your City or Locality?
[Unknown]:  Perm
What is the name of your State or Province?
[Unknown]:  Perm
What is the two-letter country code for this unit?
[Unknown]:  RU
Is CN=Cherepakhin, OU=Vasili OU, O=Vasili Organization, L=Perm, ST=Perm, C=RU correct?
[no]:  yes

Generating 3072-bit RSA key pair and self-signed certificate (SHA384withRSA) with a validity of 365 days
for: CN=Cherepakhin, OU=Vasili OU, O=Vasili Organization, L=Perm, ST=Perm, C=RU
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
Пароль не вводил.

Информация о сертификате при запуске с v.perm.ru [https://www.misterpki.com/keytool-list-certs/](https://www.misterpki.com/keytool-list-certs/):

````text
Common Name (CN)	Cherepakhin
Organization (O)	Vasili Organization
Organizational Unit (OU)	Vasili OU
Common Name (CN)	Cherepakhin
Organization (O)	Vasili Organization
Organizational Unit (OU)	Vasili OU
Issued On	Wednesday, April 1, 2026 at 10:15:07PM
Expires On	Thursday, April 1, 2027 at 10:15:07PM
````

Просмотр keystore:

````shell
$ cat https.sh 
$ keytool -list -keystore src/main/resources/keystore.p12
Enter keystore password: нет пароля

*****************  WARNING WARNING WARNING  *****************
* The integrity of the information stored in your keystore  *
* has NOT been verified!  In order to verify its integrity, *
* you must provide your keystore password.                  *
  *****************  WARNING WARNING WARNING  *****************

Keystore type: PKCS12
Keystore provider: SUN

Your keystore contains 1 entry

Alias name: tomcat
Creation date: Apr 3, 2026
Entry type: PrivateKeyEntry
Certificate chain length: 0

````

````shell
keytool -list -v -alias tomcat -keystore src/main/resources/keystore.p12
pass: пусто

Alias name: tomcat
Creation date: Apr 3, 2026
Entry type: PrivateKeyEntry
Certificate chain length: 0
````

````shell
$ openssl s_client -connect v:443 -showcerts
CONNECTED(00000003)
Can't use SSL_get_servername
depth=2 C = US, O = Internet Security Research Group, CN = ISRG Root X1
verify return:1
depth=1 C = US, O = Let's Encrypt, CN = E7
verify return:1
depth=0 CN = v.perm.ru
verify return:1
---
Certificate chain
 0 s:CN = v.perm.ru
   i:C = US, O = Let's Encrypt, CN = E7
   a:PKEY: id-ecPublicKey, 256 (bit); sigalg: ecdsa-with-SHA384
   v:NotBefore: Feb 16 16:24:34 2026 GMT; NotAfter: May 17 16:24:33 2026 GMT
-----BEGIN CERTIFICATE-----
....
-----END CERTIFICATE-----

1 s:CN = v.perm.ru
   i:C = US, O = Let's Encrypt, CN = E7
   a:PKEY: id-ecPublicKey, 256 (bit); sigalg: ecdsa-with-SHA384
   v:NotBefore: Feb 16 16:24:34 2026 GMT; NotAfter: May 17 16:24:33 2026 GMT
-----BEGIN CERTIFICATE-----
....
-----END CERTIFICATE-----
 2 s:C = US, O = Let's Encrypt, CN = E7
   i:C = US, O = Internet Security Research Group, CN = ISRG Root X1
   a:PKEY: id-ecPublicKey, 384 (bit); sigalg: RSA-SHA256
   v:NotBefore: Mar 13 00:00:00 2024 GMT; NotAfter: Mar 12 23:59:59 2027 GMT
-----BEGIN CERTIFICATE----
....
-----END CERTIFICATE-----
---
Server certificate
subject=CN = v.perm.ru
issuer=C = US, O = Let's Encrypt, CN = E7
...
````

От gigachat:

````text
Запрос:
Работает https сервер apache2 на linux. Есть доступ к нему по https. Есть cert.pem  chain.pem  fullchain.pem  keystore.p12  privkey.pem. Нужно запустить spring boot приложение на этом сервере. Какие настройки нужно сделать в application.yaml этого приложения?

Ответ:
Для запуска Spring Boot-приложения по HTTPS с использованием ваших сертификатов и ключей, настройки в application.yaml будут следующими.

Пример конфигурации для application.yaml

server:
  port: 8443 # или любой другой порт, который вы хотите использовать
  ssl:
    enabled: true
    key-store: /path/to/keystore.p12
    key-store-type: PKCS12
    key-store-password: your_keystore_password # укажите пароль от keystore.p12
    key-alias: your_key_alias # если есть, иначе можно не указывать
Пояснения
key-store: путь к вашему файлу keystore.p12.
key-store-type: тип хранилища ключей. Для .p12 — это PKCS12.
key-store-password: пароль для доступа к keystore.
key-alias: псевдоним ключа внутри keystore (если он есть, иначе можно не указывать).
Если вы хотите использовать отдельные файлы (privkey.pem и fullchain.pem)
Spring Boot также поддерживает конфигурацию через отдельные файлы ключа и сертификата:

server:                 <----- МОЙ ВАРИАНТ
  port: 8443
  ssl:
    enabled: true
    key-store-type: PKCS12
    key-store: /path/to/keystore.p12
    key-store-password: your_keystore_password

# Либо так, если не используете keystore: 
# server:
#   ssl:
#     key-store-type: PKCS12
#     key-store: classpath:keystore.p12
#     key-store-password: your_keystore_password

# Или для отдельных файлов (Spring Boot 2.2+):
# server:
#   ssl:
#     key-store-type: PEM
#     ssl-certificate: file:/path/to/fullchain.pem
#     ssl-key: file:/path/to/privkey.pem
Важно: поддержка отдельных PEM-файлов появилась в Spring Boot 2.2+. Если у вас более старая версия, используйте только keystore.

Рекомендация
Если у вас уже есть keystore.p12, лучше использовать его, так как это наиболее универсальный и поддерживаемый способ для Spring Boot.
````

Запустите приложение.

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

### Перенаправление трафика через apache2
000-default-le-ssl.conf:
````text
<IfModule mod_ssl.c>
    <VirtualHost *:443>
	# The ServerName directive sets the request scheme, hostname and port that
	# the server uses to identify itself. This is used when creating
	# redirection URLs. In the context of virtual hosts, the ServerName
	# specifies what hostname must appear in the request's Host: header to
	# match this virtual host. For the default virtual host (this file) this
	# value is not decisive as it is used as a last resort host regardless.
	# However, you must set it for any further virtual host explicitly.
	ServerName v.perm.ru

	ServerAdmin vasi.che@gmail.com
	DocumentRoot /var/www/main/

	# Available loglevels: trace8, ..., trace1, debug, info, notice, warn,
	# error, crit, alert, emerg.
	# It is also possible to configure the loglevel for particular
	# modules, e.g.
	LogLevel debug

	ErrorLog ${APACHE_LOG_DIR}/error.log
	CustomLog ${APACHE_LOG_DIR}/access.log combined

	# For most configuration files from conf-available/, which are
	# enabled or disabled at a global level, it is possible to
	# include a line for only one particular virtual host. For example the
	# following line enables the CGI configuration for this host only
	# after it has been globally disabled with "a2disconf".
	#Include conf-available/serve-cgi-bin.conf

	#ServerName v.perm.ru
	SSLEngine on
	
	#SSLProxyEngine On
	#ProxyPreserveHost on
	#ProxyRequests off
	
	SSLCertificateFile /etc/letsencrypt/live/v.perm.ru/cert.pem
	#SSLCertificateFile /etc/letsencrypt/live/v.perm.ru/cert.pem
	SSLCertificateChainFile /etc/letsencrypt/live/v.perm.ru/fullchain.pem
	SSLCertificateKeyFile /etc/letsencrypt/live/v.perm.ru/privkey.pem
	SSLCACertificateFile /etc/letsencrypt/live/v.perm.ru/chain.pem
	Include /etc/letsencrypt/options-ssl-apache.conf

	# Проксирование запросов на Spring Boot (работает по HTTP на localhost:8088)
	# Снаружи доступно по https://v.perm.ru/hr_admin
	# 192.168.1.20:8888 - адрес машнины во внутренней сети с запущенным сервисом 
	ProxyPreserveHost On
	ProxyPass /hr_admin http://192.168.1.20:8088
	ProxyPassReverse /hr_admin http://192.168.1.20:8088
	# Передача информации о протоколе (важно для Spring Boot)
	RequestHeader set X-Forwarded-Proto https

	# Дополнительные настройки безопасности и логирования
	Protocols h2 http/1.1
	SSLProtocol all -SSLv3 -TLSv1 -TLSv1.1
	ErrorLog ${APACHE_LOG_DIR}/spring-proxy-error.log
	CustomLog ${APACHE_LOG_DIR}/spring-proxy.log combined
    </VirtualHost>

````

Полностью в [doc/https/000-default-le-ssl.conf](doc/https/000-default-le-ssl.conf)

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

[Диалоговое окно подтверждения](https://dev.to/andypeters/creating-a-modal-dialog-in-tailwindcss-and-alpinejs-bc5)

### Стили

- py-2 = padding 8 px (*4)
- px-3 = padding 12 px (*4)
- mb-2 = margin buttom 8 px (*4)
- w-full - растянуть на всю доступную ширину
- max-w-6xl - определение максимальной ширины
- justify-center - динамическое центрирование по вертикали
- justify-end - прижать к правому краю

### Прижать кнопки к правой стороне

__text-right__ - стиль для выравнивания текста
__justify-end__ - работает по другому принципу, чем __text-right__.

````text
    <tr id="position-${position.id}" class="border-b border-gray-100 hover:bg-gray-50 transition">
        <td class="px-4 py-2 font-medium">${position.name}</td>
        <td class="px-4 py-2 text-right">                       <!-- !!!!!!!!!!!!!!!!!!!!!! -->
            <!-- Edit Action -->
            <a ...</a>
            <!-- Delete Action with Custom Modal -->
            <button ... </button>
        </td>
    </tr>
````

### Тестирование снаружи

Screen shot tool URL [https://iotools.cloud/tool/website-screenshot/](https://iotools.cloud/tool/website-screenshot/)

### TODO

Мелкий шрифт на пагинации на телефоне.

Шрифты [https://tailwindcss.com/docs/font-size](https://tailwindcss.com/docs/font-size).

Freemaker assign value:

````text
<#assign value="\t\tweather\n\n">
<#compress>
${value}
Today is a wonderful day.
1 2   3       4     5
</#compress>
````

### Удаление

Выполнение DELETE запроса (совет [отсюда](https://stackoverflow.com/questions/24256051/delete-or-put-methods-in-thymeleaf)):

````javascript
<script th:inline="javascript">
    function sendDelete(url) {
        var xhttp = new XMLHttpRequest();
        xhttp.open("DELETE", url, true);
        xhttp.onload = function () {
            let responseURL = xhttp.responseURL;
            console.log("Redirecting to:", responseURL);
            window.location.replace(responseURL);
        };
        xhttp.send();
    }
</script>
````

Использование на страницах:

````html
<a type="button" th:with="url = @{<your_url>}" th:onclick="sendDelete([[${url}]])">Delete</a>
````


### Ветки

v0.0.5 - работает CRUD (особенно удаление) позиций и сотрудников.

### Разное

Нормально работает tools из браузера

Просмотреть скриншоты сайта:
- [https://convertico.com/website-screenshot/](https://convertico.com/website-screenshot/)
- для проверки ДОСТУПНОСТИ из web [https://convertico.com/website-screenshot/](https://convertico.com/website-screenshot/)
- [https://anytools.pro/ru/img/screenshot](https://anytools.pro/ru/img/screenshot)
- [https://screenshot.guru/](https://screenshot.guru/)
- [https://iotools.cloud/tool/website-screenshot/](https://iotools.cloud/tool/website-screenshot/)

### PageSpeedInsights

__PageSpeedInsights__ - Инструмент тестирования скорости загрузки и __качества__ html разметки [https://pagespeed.web.dev/analysis/https-v-perm-ru-hr_admin/wlfhga6g6l?hl=ru&form_factor=desktop](https://pagespeed.web.dev/analysis/https-v-perm-ru-hr_admin/wlfhga6g6l?hl=ru&form_factor=desktop)
Некоторые результаты работы PageSpeedInsights:

![doc/pagespeed/pagespeed_common.png](doc/pagespeed/pagespeed_common.png)

![doc/pagespeed/pagespeed_speed_result.png](doc/pagespeed/pagespeed_speed_result.png)

![doc/pagespeed/pagespeed_speed1.png](doc/pagespeed/pagespeed_1.png)

![doc/pagespeed/pagespeed_2.png](doc/pagespeed/pagespeed_2.png)

![doc/pagespeed/pagespeed_3.png](doc/pagespeed/pagespeed_3.png)

![doc/pagespeed/pagespeed_4.png](doc/pagespeed/pagespeed_4.png)

![doc/pagespeed/pagespeed_5.png](doc/pagespeed/pagespeed_5.png)

Просмотр commits  на github [https://github.com/cherepakhin/hr_admin/commits/v0.0.5](https://github.com/cherepakhin/hr_admin/commits/v0.0.5)

### Диалоги

Простой диалог с сообщением:
````text
                                <!-- Delete Action with Confirmation -->
                                <a href="${springMacroRequestContext.contextPath}/positions/delete/${position.id}"
                                   onclick="return confirm('Вы уверены, что хотите удалить ${position.name} ?');"
                                   class="action-delete inline-flex items-center justify-center w-5 h-5 rounded-full hover:bg-red-100 transition text-red-600"
                                   viewBox="0 0 24 24"
                                   title="Удалить">
                                    <svg class="action-icon" xmlns="https://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
                                    </svg>
                                </a>

````

Убрать скругления углов:

````text
.modal-container: border-radius: 0
.btn-cancel: border-radius: 0
.btn-delete: border-radius: 0
````

Диалог подтверждения удаления позиции:

![doc/dialog_delete_position.png](doc/dialog_delete_position.png)

[HTML код страницы с диалогом подтверждения удаления и комментариями](doc/view-source_127.0.0.1_8088_hr_admin_positions_.html)
 
Стиль "gap-4" - уместить 4 элемента  

### Экраны для мобильных устройств

![doc/mobile_screens/all_employees.png](doc/mobile_screens/all_employees.png)

![doc/mobile_screens/delete_employee.png](doc/mobile_screens/delete_employee.png)

![doc/mobile_screens/find_employees.png](doc/mobile_screens/find_employees.png)

![doc/mobile_screens/list_employees.png](doc/mobile_screens/list_employees.png)

![doc/mobile_screens/new_employee.png](doc/mobile_screens/new_employee.png)

![doc/mobile_screens/positions.png](doc/mobile_screens/positions.png)

### Включено логирование SQL запросов в application.yaml:

````yaml
logging:
  level:
    org.hibernate.SQL: DEBUG
    org.hibernate.orm.jdbc.bind: trace
````

Log:
````text
Hibernate: 
    insert 
    into
        employees
        (email, first_name, last_name, position_id, id) 
    values
        (?, ?, ?, ?, default)
23:54:07.281+05:00 TRACE 16885 --- [           main] org.hibernate.orm.jdbc.bind              : binding parameter (1:VARCHAR) <- [emp9@example.com]
23:54:07.282+05:00 TRACE 16885 --- [           main] org.hibernate.orm.jdbc.bind              : binding parameter (2:VARCHAR) <- [Name 9]
23:54:07.283+05:00 TRACE 16885 --- [           main] org.hibernate.orm.jdbc.bind              : binding parameter (3:VARCHAR) <- [Lastname 9]
23:54:07.293+05:00 TRACE 16885 --- [           main] org.hibernate.orm.jdbc.bind              : binding parameter (4:BIGINT) <- [3]
23:54:07.301+05:00  INFO 16885 --- [           main] n.g.s.f.initializer.DataInitializer      : Test data added.
````

### Запуск как сервис в Linux

Описано здесь [Autostart сервиса в linux](https://v.perm.ru/index.php/instrumenty-devops/autostart-service).

Создать файл hr_admin.service в /etc/systemd:

````text
[Unit]
Description=HR admin
Wants=network-online.target
After=network-online.target
[Service]
Type=simple
User=vasi
Group=vasi
ExecReload=/bin/kill -HUP 
ExecStart=/home/vasi/temp/hr_admin.sh
SyslogIdentifier=hr_admin
Restart=always
[Install]
WantedBy=multi-user.target
````

/home/vasi/temp/hr_admin.sh:

````shell
/usr/lib/jvm/java-17-openjdk-amd64/bin/java -jar /home/vasi/temp/hr-admin-0.0.5.jar --server.port=8088
````

Перечитать сервисы:

````shell
sudo systemctl daemon-reload
````

Включить сервис:

````shell
systemctl enable hr_admin.service
````

Запустить сервис:

````shell
systemctl start hr_admin.service
````

### Подключение к MCP сервису для VS Code

Создать каталог .codeassistant в каталоге проекта. В нем создать файл mcp.json:

````json
{
  "mcpServers": {
    "github": {
      "type": "stdio",
      "command": "docker",
      "args": [
        "run",
        "-i",
        "--rm",
        "-e",
        "GITHUB_PERSONAL_ACCESS_TOKEN",
        "-e",
        "GITHUB_TOOLSETS",
        "-e",
        "GITHUB_READ_ONLY",
        "ghcr.io/github/github-mcp-server"
      ],
      "env": {
        "GITHUB_PERSONAL_ACCESS_TOKEN": "ТОКЕН_GITHUB",
        "GITHUB_TOOLSETS": "",
        "GITHUB_READ_ONLY": ""
      }
    }
  }
}
````

### Задание RULES для gigacode агента

Правил находятся в файле [.gigacode/rules/hr_admin_rules.md](.gigacode/rules/hr_admin_rules)

````text
# Правила проекта
## Архитектура
- Используй слоистую архитектуру: controller, service, repository, model, dto.
- Бизнес-логика должна находиться в service-слое.
- Controller не должен напрямую обращаться к repository.
- названия методов должны быть глаголами в форме глагол + объект.
## Тестирование
- Для всех новых публичных методов создавай unit-тесты.
- Для API-изменений добавляй интеграционные тесты.
- Используй существующий стиль тестов проекта.
## Безопасность
- Не читай и не изменяй файлы .env, credentials.json, secrets.yaml.
- Не выводи секреты и токены в ответах.
- При изменении авторизации указывай возможные риски.
````

(скачал откуда-то)

Итоговые правила (prompt: "покажи rules для агента"):

````text
# HR Admin Rules

## Архитектура
- Используй слоистую архитектуру: controller, service, repository, model, dto
- Бизнес-логика должна находиться в service-слое
- Controller не должен напрямую обращаться к repository
- Названия методов должны быть глаголами в форме "глагол + объект" (например: `createEmployee`, `deletePosition`)

## Тестирование
- Для всех новых публичных методов создавай unit-тесты
- Для API-изменений добавляй интеграционные тесты
- Используй существующий стиль тестов проекта (JUnit 5 + Mockito)
- Тесты пишутся в snake_case или camelCase для методов (например: `shouldListEmployeesWithPagination`, `create_employee_and_redirect`)

## Безопасность
- Не читай и не изменяй файлы `.env`, `credentials.json`, `secrets.yaml`
- Не выводи секреты и токены в ответах
- При изменении авторизации указывай возможные риски

## Код-стайл (AGENTS.md)
- Индентация: 4 пробела
- Максимальная длина строки: 120 символов
- Используй IntelliJ IDEA default code style для Java
- Используем Lombok: `@RequiredArgsConstructor` для DI, `@Slf4j` для логирования
- Предпочитаем инициализацию final полей через конструктор
- Избегай `var` — предпочитай явные типы
- Избегай мутаций объектов, особенно в foreach и Stream API
- Используй ранний выход (early return) где возможно
- Избегай методов с `throws` — используй unchecked exceptions
- Контроллеры называются `...Controller`, сервисы — `...Service`, репозитории — `...Repository`
- Для шаблонов используем FreeMarker с расширением `.ftlh`
- View-имена храним в интерфейсе `NamesView`
````

Из GigaIde в облаке https://gigaide-dc9f0332-248f-4c32-954e-d029312d17ee.containerapps.ru/proxy/8088/hr_admin/https://gigaide-dc9f0332-248f-4c32-954e-d029312d17ee.containerapps.ru/proxy/8088/hr_admin/

### Ввод голосом

На мобильных телефонах значения в текстовые поля можно вводить голосом при нажатии на иконку микрофона в правом верхнем углу клавиатуры.