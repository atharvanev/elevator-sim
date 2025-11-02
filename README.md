# Elevator Simulation
### Key Functionality

* The elevator size and capacity can be set by the simulation
* The building size can also be set by the simulation
* The Elevator can take realtime updates as its moving up and down through the Elevator

### Assumptions/Not Implemented

* Riders will only get on the elevator unless it is following the direction they want to travel in
* The Elevator Class and Building Class will be run in a Simulation that calls a step function to simulate time passing for the Elevator
* The Building Logic and simulation display currently support only one elevator, but are designed to allow future expansion
* Passengers instantly board and exit the elevator (no loading or unloading delay)
* Passengers cannot cancel or modify their requests once added
* Elevator doors are assumed to always open and close instantly 



## Run Instructions

Go to the correct file location
```commandline
cd src/main/java 
```
Compile the code
```commandline
javac com/atharva/elevator/*.java
```
Run the compiled Code
```commandline
 java com.atharva.elevator.ElevatorSimulation
```

JAVA version
```commandline
openjdk 17.0.17 2025-10-21 LTS
OpenJDK Runtime Environment Microsoft-12574438 (build 17.0.17+10-LTS)
OpenJDK 64-Bit Server VM Microsoft-12574438 (build 17.0.17+10-LTS, mixed mode, sharing)
```