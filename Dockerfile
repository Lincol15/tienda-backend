# Imagen con JDK 17 para compilar y ejecutar
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copiamos configuración de Maven primero (mejor uso de caché)
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .

# Arreglamos finales de línea y permisos del wrapper (evita errores en Linux)
RUN apt-get update && apt-get install -y dos2unix && \
    dos2unix mvnw 2>/dev/null || true && \
    chmod +x mvnw

# Copiamos el código fuente
COPY src src

# Compilamos (sin tests para que el build sea más rápido)
RUN ./mvnw clean package -DskipTests

# La app escucha en 8080 por defecto (application.properties)
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar target/*.jar"]
