FROM openjdk
WORKDIR shortcut
ADD target/url_shortcut-1.0.jar app.jar
ENTRYPOINT java -jar app.jar