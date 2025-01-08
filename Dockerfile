FROM eclipse-temurin:17.0.13_11-jre-ubi9-minimal
COPY target/mobile-number-portability-1.0.0.jar mobile-number-portability-1.0.0.jar
ENTRYPOINT ["java","-jar","/mobile-number-portability-1.0.0.jar"]