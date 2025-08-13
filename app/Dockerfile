# Stage 1: Build the application
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar archivos de configuración y dependencias
COPY pom.xml .
COPY src ./src

# Compilar la aplicación y generar el JAR
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiar el JAR compilado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto de la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

