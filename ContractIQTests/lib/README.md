# Library Folder

Place all Selenium JAR files here.

## Required JARs

Download Selenium Java from: https://www.selenium.dev/downloads/

After downloading and extracting, copy ALL these JARs:

### Main JAR
- selenium-java-4.x.x.jar

### From the 'libs' subfolder (copy ALL of them):
- selenium-api-4.x.x.jar
- selenium-chrome-driver-4.x.x.jar
- selenium-chromium-driver-4.x.x.jar
- selenium-devtools-v*.jar
- selenium-edge-driver-4.x.x.jar
- selenium-firefox-driver-4.x.x.jar
- selenium-http-4.x.x.jar
- selenium-json-4.x.x.jar
- selenium-manager-4.x.x.jar
- selenium-remote-driver-4.x.x.jar
- selenium-support-4.x.x.jar

### Additional dependencies (also in libs folder):
- auto-service-*.jar
- byte-buddy-*.jar
- commons-exec-*.jar
- failsafe-*.jar
- guava-*.jar
- httpcore5-*.jar
- httpclient5-*.jar
- j2objc-annotations-*.jar
- jspecify-*.jar
- listenablefuture-*.jar
- opentelemetry-*.jar
- slf4j-api-*.jar
- websocket-*.jar
- ... and all other JARs in that folder

## Important
- Copy ALL JARs, not just some of them
- Missing JARs will cause ClassNotFoundException errors
