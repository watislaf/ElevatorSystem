# Elevator System

This project simulates elevators in a building. The project is divided into two parts, the Server part that manipulates objects
and the Client part that draws them in real time. 

Github actions and docker was used to atomatically deploy the project to the server.

___

<p align=center>
  <img  src="https://github.com/watislaf/ElevatorSystem/blob/main/outpup1.gif" width="40%"> 
  </p>
 
<p align=center>
  <img  src="https://github.com/watislaf/ElevatorSystem/blob/main/outpup2.gif"  width="70% " >
  </p>


## Development status

Still a lot of work to do.
## Install

Download source files.

```bat
  git clone https://github.com/watislaf/ElevatorSystem.git # install
  cd ./ElevatorSystem
```

if you don't have git, just download source
from [this page](https://github.com/watislaf/chessbot/releases/tag/V1.0.1600Elo).
____
## Build /-> Run

### Gradle

To build and run project u can either use gradle (if u have installed one)

```bat
  ./gradlew :app:buld -Dorg.gradle.java.home="PATH_TO/openjdk-17.0.2"
  ./gradlew :app:run -Dorg.gradle.java.home="PATH_TO/openjdk-17.0.2"
  ./gradlew :window:buld -Dorg.gradle.java.home="PATH_TO/openjdk-17.0.2"
  ./gradlew :window:run -Dorg.gradle.java.home="PATH_TO/openjdk-17.0.2"
 ```
____
### Docker

or use docker.

```bat
  sudo bash ./setupDocker  
```

### Docker is not installed

```bat
  sudo bash ./installDocker  
```
_____

 
### Authors

* Vladislav Kozulin ([@watislaf](https://github.com/watislaf))
