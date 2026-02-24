# Usa una imagen que soporte Java 17
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copia todos los archivos de tu proyecto
COPY . .

# ARREGLA PERMISOS Y FORMATO (Esto evita el "Permission Denied")
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# CONSTRUYE EL PROYECTO (Genera el archivo .jar)
RUN ./mvnw clean package -DskipTests

# Expone el puerto de Spring
EXPOSE 8080

# EJECUTA EL JAR GENERADO
CMD ["sh", "-c", "java -jar target/*.jar"]
