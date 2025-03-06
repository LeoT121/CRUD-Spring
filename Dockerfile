# Usamos una imagen oficial de OpenJDK como base
FROM openjdk:21-jdk-slim

# Configurar el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el JAR generado en el contenedor
COPY target/*.jar app.jar

# Expone el puerto en el que corre la aplicación
EXPOSE 8080

# Ejecuta la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
