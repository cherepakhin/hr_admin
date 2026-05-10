#!/bin/bash

# open http://127.0.0.1:8088/hr_admin/
export JAVA_HOME=/usr/lib/jvm/java-1.17.0-openjdk-amd64
export SERVER_SERVLET_CONTEXT_PATH=/hr_admin
# -Dserver.servlet.context-path="/hr_admin"
./mvnw clean spring-boot:run
