# --- Stage 1: Build ---
FROM eclipse-temurin:21-jdk AS build
LABEL authors="oceanyu"

WORKDIR /app

# 1. Copy only the files needed to download dependencies
COPY mvnw .
COPY .mvn ./.mvn
COPY pom.xml .

# 2. Grant permission and download dependencies (Cached Layer!)
RUN chmod +x ./mvnw
# This command downloads dependencies without building the app
RUN ./mvnw dependency:go-offline -B

# 3. NOW copy the source code (This layer changes frequently)
COPY src ./src

# 4. Build the app (Offline, because we already downloaded deps)
RUN ./mvnw clean package -DskipTests

# --- Stage 2: Run ---
# USE JRE HERE (Smaller image)
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]