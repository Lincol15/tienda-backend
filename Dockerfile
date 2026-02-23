# 1. Usamos una imagen ligera de Java
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# 2. Copiamos todos los archivos del proyecto
COPY . .

# 3. Limpiamos caracteres de Windows y damos permisos (Vital para que no falle)
RUN sed -i 's/\r$//' mvnw
RUN chmod +x mvnw

# 4. CONSTRUIMOS EL PROYECTO (Esta línea faltaba en tu último commit)
RUN ./mvnw clean package -DskipTests

# 5. Exponemos el puerto
EXPOSE 8080

# 6. Arrancamos la aplicación buscando cualquier archivo .jar que se haya creado
CMD ["sh", "-c", "java -jar target/*.jar"]
