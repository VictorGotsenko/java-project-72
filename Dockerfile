FROM gradle:8.7-jdk21

WORKDIR /app

COPY /app .

RUN ./gradlew --no-daemon build

CMD java -jar build/libs/app-1.0-SNAPSHOT-all.jar
