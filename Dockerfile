# Usamos una imagen de Java ligera
FROM eclipse-temurin:17-jdk-alpine

# Definimos el directorio de trabajo
WORKDIR /app

# Copiamos todos los archivos del proyecto al contenedor
COPY . .

# CORRECCIÓN VITAL: 
# 1. Quitamos caracteres ocultos de Windows (\r)
# 2. Damos permiso de ejecución al Maven Wrapper
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# COMPILACIÓN: Generamos el archivo .jar
RUN ./mvnw clean package -DskipTests

# Exponemos el puerto que usa Spring Boot por defecto
EXPOSE 8080

# ARRANCAR: Ejecutamos el archivo generado en la carpeta target
CMD ["sh", "-c", "java -jar target/*.jar"]
