# Imagen con JDK 17 para compilar y ejecutar
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copiamos configuración de Maven primero (mejor uso de caché)
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .

# Arreglamos finales de línea (Windows -> Linux) sin instalar paquetes; sed viene en la imagen
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Copiamos el código fuente
COPY src src

# Compilamos (sin tests para que el build sea más rápido)
RUN ./mvnw clean package -DskipTests

# La app escucha en 8080 por defecto (application.properties)
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar target/*.jar"]
