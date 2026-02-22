FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

# ESTA ES LA LÍNEA QUE DEBES AGREGAR:
RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

EXPOSE 8080

# Usamos un comodín (*) para que encuentre el JAR sin importar el nombre exacto
CMD ["sh", "-c", "java -jar target/*.jar"]
