# 1. Imagen base
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# 2. Copiamos TODO el proyecto
COPY . .

# 3. Arreglamos el archivo mvnw para que Linux lo entienda
# (Elimina espacios de Windows y da permisos de ejecución)
RUN sed -i 's/\r$//' mvnw
RUN chmod +x mvnw

# 4. COMPILAMOS (Esta es la línea que faltaba en tu último error)
RUN ./mvnw clean package -DskipTests

# 5. Puerto estándar
EXPOSE 8080

# 6. Ejecutamos el archivo generado
CMD ["sh", "-c", "java -jar target/*.jar"]
