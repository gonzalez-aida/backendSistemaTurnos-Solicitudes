# Utiliza una imagen base de java
FROM eclipse-temurin:17-jre-alpine

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo JAR de la aplicación
COPY target/backS-0.0.1-SNAPSHOT.jar /app/ms-solicitudes.jar

# Expone el puerto de la aplicación (ej. 8080)
EXPOSE 8086

# Define la entrada para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "ms-solicitudes.jar"]