@echo off
set JAVA_HOME=C:\po\jvm\jdk17
call mvnw clean package -DskipTests
