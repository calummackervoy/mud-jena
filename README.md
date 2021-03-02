

# Multi User Domain - Jena Project

The goal of this project is to provide a set of Java utilities to serve resources for the Multi User Domain platform

Using the semantic web technologies, it should be possible to build a Multi User Domain which 

Tech Stack:
* Java 8-11
* Apache Tomcat 9
* Jersey
* Apache Jena 3

## Separation of Concerns

The framework is currently in only one repository, but is being built with a separation of concerns in mind:

* World Server. Responsible for the physical (shared) objects of a world. Largely just intended to be a layer to protect the data store and define the rules for interaction
* The User's POD: Users store their data themselves, via [Solid Pods](https://inrupt.com/solid/). This allows them to take characters between worlds, for example. Not provided by this library, relies on the client-side.
* Content Server. The eyes and ears of the player. A client requests from it to describe a particular object, and it will return the associated content which it can from it. (E.g. there may be a game on at the stadium, so I will describe it as busy). A key design concern is that one client may be pulling data from multiple content servers
* Action Server. Provides endpoints allowing the user to act. E.g. I may want to build a house in a settlement. I would POST the Action Server and it should manage this for me
* TODO: Simulation Servers: programs which push content to other servers but which are not players. For example a Sim server which expands populations at a realistic rate, or which randomly packs it with gangs of anarchist bank robbers

By separating these concerns it allows for greater modularity and use of the application. For example I may want to define a World Server and see the interesting ways in which players and programs will change it over time, without wanting to concern myself with providing rich descriptions of the world. I may want to produce Action Server content which allows people to build things in other worlds

# Getting Started Locally

Contact us if you have any difficulties!

## Installation

* Install Tomcat 9: https://linuxize.com/post/how-to-install-tomcat-9-on-ubuntu-20-04/ (Ubuntu)
* Install Eclipse IDE for Java EE developers. http://needhamia.com/?page_id=346
* Clone the repository locally
* Import the project into your Eclipse workspace
* We use Maven for the install dependencies. You should be able to install the requirements with this

## Running Tomcat server from Eclipse

* In Eclipse
* In the Project Explorer, right-click on the MUD project and select Properties
* In the resultant window, select Targeted Runtimes
* Click New
* Select Apache Tomcat v9.0
* Check the box labeled Create a new local server
* Click Next
* In the Tomcat Installation directory field, type the value of your CATALINA_HOME environment variable. For example following the previous tutorial it would be /opt/tomcat/latest/
* Click Finish
* In the Targeted Runtimes dialog, click (mark the checkbox) the newly-created Apache Tomcat v9.0 entry.
* Click OK
* Now the Project Explorer pane should show two entries: one is the project; the other is the test server for your project.
* Select Window / Show View / Servers. That should show your Tomcat test server, whether it needs to be updated (“republish”) and whether it’s running or stopped. Right click it and click "debug"

### Useful info to newcomers to Java servers/Tomcat

The file **web.xml** holds the Tomcat configuration for the local server (src/main/webapp/WEB-INF/web.xml)

The file **pom.xml** holds the Maven dependencies for the project

## Running Unit Tests

We use JUnit tests (and sometimes the Jersey extension `JerseyTest`)

# Docker

You can build and run the application using the Dockerfile. Building the image requires no specific build args, so can be done simply with:
```
docker build . -t mud-jena:<branch>
```

To run the image, simply bind port 8080 to a local port:
```
docker run -p 8080:8080  mud-jena:<branch>
```

## CI

We run Docker build on PRs in this repo (restricted to contributors only for security reasons), so if you want to checkout a PR or master, you can use our prebuilt images like so:
```
docker run -p 8080:8080 multiuserdomain/mud-jena:master

docker run -p 8080:8080 multiuserdomain/mud-jena:<branch-name>
```

# Deploying to a production Tomcat server

* In Eclipse, in the Project Explorer, right-click on the MUD project name and select Export / WAR file
* For Destination, click the Browse button
* Navigate to the folder you wish to save the .war file to, choose what to name it, and click Save
* Check Export Source Files. Without this option, your html files would not be included in the .war file.
* Click Finish
* Add the project WAR file to your server

# Contributing

It's early days for now, please get in touch with us directly and we'll help you set up and get involved in the project :-)
