@echo off
rem open http://127.0.0.1:8989/hradmin/
set JAVA_HOME=C:\Program Files\Java\jdk-17
set SERVER_SERVLET_CONTEXT_PATH=/hradmin
call mvnw clean spring-boot:run
