FROM amazoncorretto:17-alpine-jdk
WORKDIR /app
RUN addgroup --system javauser && adduser -S -s /usr/sbin/nologin -G javauser javauser
COPY target/*.jar app.jar
RUN chown -R javauser:javauser .
USER javauser
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "app.jar" ]
