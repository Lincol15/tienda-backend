FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copiamos todo el contenido del repositorio al contenedor
COPY . .

# Corregimos los permisos de fin de línea (por si subiste desde Windows) 
# y damos permiso de ejecución
RUN apt-get update && apt-get install -y dos2unix && \
    dos2unix mvnw && \
    chmod +x mvnw

# Ejecutamos la compilación
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

# Comando para arrancar la aplicación usando el shell para expandir el asterisco
CMD ["sh", "-c", "java -jar target/*.jar"]
