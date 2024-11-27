FROM sbtscala/scala-sbt:eclipse-temurin-jammy-22_36_1.10.0_3.4.2

RUN apt update && \
    apt install -y \
    default-jdk

WORKDIR /app

CMD ["sbt", "run"]
