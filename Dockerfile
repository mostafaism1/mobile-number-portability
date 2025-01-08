FROM eclipse-temurin:17.0.13_11-jre-ubi9-minimal
COPY target/mobile-number-portability-0.0.1-SNAPSHOT.jar mobile-number-portability-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/mobile-number-portability-0.0.1-SNAPSHOT.jar"]