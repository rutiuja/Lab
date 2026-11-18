FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY src/main/java/HealthApplication.java .
RUN javac --add-modules jdk.httpserver HealthApplication.java

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/HealthApplication*.class .
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD ["java", "--add-modules", "jdk.httpserver", "HealthApplication", "--check"]
CMD ["java", "--add-modules", "jdk.httpserver", "HealthApplication"]
