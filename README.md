# Spring Game Dashboard & Server
*A simple Spring Boot REST-based game client dashboard and server implementation*

## Overview

This project contains both `game-client` and `game-server`, communicating over REST and testing out basic functions related to starting up and managing a match-based game server with player persistence.

This is meant to provide basic backend and frontend infrastructure to easily test a functional game server API with a robust JavaFX-based dashboard that can be easily refitted into working as a fully functional game client.

The server contains methods managing **match lifecycles**, **player registration**, **queuing**, and **player data requests**—reducing the need to thoroughly troubleshoot and implement such from scratch.

Essentially, the Dashboard and Server serve as a robust template for easily implementing 1v1 games using REST, Spring Boot, and Java.

## Running Project

### Dependencies

The project requires that these be installed prior to running:

* Java 21 or later ([Eclipse Temurin JDK](https://adoptium.net/temurin/releases?version=21&os=any&arch=any) recommended)
* [Maven](https://maven.apache.org/)
* [JavaFX](https://openjfx.io/)

It is highly recommended that you run and modify this codebase using [IntelliJ IDEA](https://www.jetbrains.com/idea/download).

Before running either module, build the entire project from the root directory:

```bash
mvn clean install
```

### Running the Client

Prior to running the client, you must first allow Maven to install the project's required dependencies.

```bash
cd game-client && mvn clean install
```

After Maven finishes installing, you may then start the client through JavaFX's Maven Plugin:

```bash
mvn javafx:run
```

### Running the Server

Prior to running the server, you must first allow Maven to install the project's required dependencies.

```bash
cd game-server && mvn clean install
```

You must also ensure that `localhost:8080` is not currently locked by any program.

```bash
# These commands may require administrator/sudo privileges.
# Double-check any and all tasks being killed in your operating system's system monitor.

# Windows
netstat -ano | findstr :8080
taskkill /pid [PID] /f

# macOS / Linux 
lsof -i :8080
kill -9 [PID]
```

Afterward, you may finally start the server using Spring Boot's Maven Plugin:
```bash
mvn spring-boot:run
```


## Running Tests

### Client

Prior to running the client's JUnit tests, please ensure that you have followed the steps to set up the client in [the previous section](#running-the-client).

Afterward, you can run the client's JUnit tests using Maven:

```bash
cd game-client && mvn test
```

### Server

Prior to running the server's JUnit tests, please ensure that you have followed the steps to set up the server in [the previous section](#running-the-server).

Afterward, you can run the server's JUnit tests using Maven:

```bash
cd game-server && mvn test
```


## Modules

| Module Topic                                           | Feature/Purpose                                                                                                                                     | Code File Path                                                                                      | Test File Path                                                                           |
|--------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------|
| Module 1: Arrays + OO Refresh                          | Allows the server to send the top N best players to the client by truncating the leaderboard and sorting.                                           | `game-server/src/main/java/edu/sdccd/cisc191/service/MatchmakingService.java`, lines [119-125]      | `game-server/src/test/java/edu/sdccd/cisc191/Module1And5And6Test.java`, lines [34-46]    |
| Module 2: OO Design + Functional Interfaces            | Provides an easy to use, robust fluent API that reduces boilerplate and ensures compile-time safety.                                                | `game-client/src/main/java/edu/sdccd/cisc191/client/net/HttpRequestExecutor.java`, lines [12-80]    | `game-client/src/test/java/edu/sdccd/cisc191/client/Module2and7Test.java`, lines [25-60] |
| Module 3: Inheritance + Polymorphism                   | TBD                                                                                                                                                 |                                                                                                     |                                                                                          |
| Module 4: Exceptions + File I/O + Database Persistence | Allows the server to persist information about players during runtime using Spring JPA and H2.                                                      | `game-server/src/main/java/edu/sdccd/cisc191/repository/PlayerAccountRepository.java`, lines [9-12] | `game-server/src/test/java/edu/sdccd/cisc191/Module4Test.java`, lines [18-34]            |
| Module 5: Recursion + Algorithms                       | Allows the server to efficiently find players by username through the use of a binary search algorithm.                                             | `game-server/src/main/java/edu/sdccd/cisc191/service/MatchmakingService.java`, lines [127-148]      | `game-server/src/test/java/edu/sdccd/cisc191/Module1And5And6Test.java`, lines [49-52]    |
| Module 6: Collections + Generics + Advanced Streams    | Allows the server to compile a leaderboard of players sorted descending by ratings robustly and efficiently using Java's Stream API and collectons. | `game-server/src/main/java/edu/sdccd/cisc191/service/MatchmakingService.java`, lines [106-116]      | `game-server/src/test/java/edu/sdccd/cisc191/Module1And5And6Test.java`, lines [54-79]    |
| Module 7: JavaFX + Events + Lambdas                    | Provides a functional client UI for testing server functionality, as well as potentially allows a more proper client implementation.                | `game-client/src/main/java/edu/sdccd/cisc191/client/GameClientApplication.java`, lines [26-40]      | `game-client/src/test/java/edu/sdccd/cisc191/client/Module2and7Test.java`, lines [62-77] |


## Reflections

### What I am most proud of:
> My proudest achievement in this codebase is fine-tuning the API used for `game-client`'s UI and networking code. One of my top priorities while working on the project was ensuring that the API for managing JavaFX's lifecycles and network requests was as robust and painless to use as possible. Their current implementation allows for greater code reusability, readability, as well as very quick, robust implementations down the line that require minimal, if any architectural changes. 

### What I would improve with more time:
> If I had more time, I would definitely have worked more on the server infrastructure in order to make matches work in real-time with gRPC. I have previously attempted to do this in the now-archived `unstable/stream-system` branch, but unfortunately much of that code was unstable, prone to memory leaks, and difficult to debug, leading me to switch to a REST backend. If I had time to revise the server code, I would attempt to make gRPC concurrency and active matches a top priority.
