FROM openjdk:21-jdk

LABEL maintainer="Rychu"

COPY target/InvoiceMicroservice-0.0.1-SNAPSHOT.jar InvoiceMicroservice-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "InvoiceMicroservice-0.0.1-SNAPSHOT.jar"]