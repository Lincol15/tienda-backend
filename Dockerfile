# Usamos una imagen que ya incluye herramientas de compilación
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copiamos solo los archivos de configuración primero para aprovechar la caché
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Limpieza total: convertimos finales de línea y damos permisos
# Usamos comandos de shell directamente para evitar errores de formato
RUN apt-get update && apt-get install -y dos2unix && \
    dos2unix mvnw && \
    chmod +x mvnw

# Copiamos el resto del código
COPY src src

# Compilamos
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

# Usamos una forma más segura de ejecutar el JAR
ENTRYPOINT ["sh", "-c", "java -jar target/*.jar"]
