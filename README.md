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