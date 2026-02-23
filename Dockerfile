FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copiamos todo
COPY . .

# Eliminamos posibles problemas de formato de Windows (\r) y damos permisos
RUN sed -i 's/\r$//' mvnw
RUN chmod +x mvnw

# Compilamos usando la memoria de forma eficiente
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

# Comando para correr el programa
CMD ["sh", "-c", "java -jar target/*.jar"]
