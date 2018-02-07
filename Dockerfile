FROM java
WORKDIR /app
ADD app.jar /app/app.jar
CMD java -jar app.jar
