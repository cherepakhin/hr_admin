keytool -genkeypair -alias tomcat \
  -keyalg RSA \
  -keysize 2048 \
  -storetype PKCS12 \
  -keystore src/main/resources/keystore.p12 \
  -validity 365 \
  -storepass changeit \
  -keypass changeit \
  -dname "CN=v.perm.ru, OU=IT Department, O=Company LLC, L=Perm, ST=Perm Krai, C=RU" \
  -ext san=dns:v.perm.ru,ip:46.146.232.50