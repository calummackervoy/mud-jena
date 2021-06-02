FROM maven:3.6.3-jdk-11 as maven_deps

WORKDIR /app

ADD pom.xml pom.xml

RUN mvn clean

RUN mvn dependency:go-offline

ADD src/main src/main

RUN mvn package 

FROM tomcat:9.0.43-jdk11 as server

RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY --from=maven_deps /app/target/mud.war /usr/local/tomcat/webapps/ROOT.war
COPY --from=maven_deps /app/pom.xml /usr/local/tomcat/webapps/pom.xml

CMD ["catalina.sh", "run"]