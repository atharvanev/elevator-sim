package com.atharva.elevator;

public class ElevatorSimulation {
    private Building building;
    private Elevator elevator;
    private int currentStep;

    public ElevatorSimulation(int numFloors, int elevatorCapacity) {
        this.building = new Building(numFloors);
        this.elevator = new Elevator(0, elevatorCapacity, 0);
        this.currentStep = 0;
    }

    // Add a passenger request
    public void addRequest(int fromFloor, int toFloor) {
        building.addPassenger(fromFloor, new Request(fromFloor, toFloor));
    }

    // Run one step of simulation
    public void step() {
        System.out.printf("Step %d: Floor %d | Direction: %s | Load: %d/%d | Destinations: %s%n",
                currentStep,
                elevator.getFloor(),
                getDirectionString(),
                elevator.getCurrentCapacity(),
                elevator.getCapacity(),
                elevator.hasDestinations() ? "Yes" : "No"
        );

        elevator.step(building);
        currentStep++;
    }

    // Run simulation until complete
    public void run(int maxSteps) {
        System.out.println("=== Starting Elevator Simulation ===\n");

        while (currentStep < maxSteps) {
            step();

            // Check if done
            if (isSimulationComplete()) {
                System.out.println("\n=== All passengers delivered! ===");
                System.out.println("Total steps: " + currentStep);
                break;
            }
        }

        if (currentStep >= maxSteps) {
            System.out.println("\n=== Max steps reached ===");
        }
    }

    // Check if simulation is complete
    private boolean isSimulationComplete() {
        // No passengers in elevator and no one waiting
        return elevator.isEmpty() &&
                elevator.isIdle() &&
                !hasWaitingPassengers();
    }

    // Check if anyone is waiting anywhere
    private boolean hasWaitingPassengers() {
        for (int floor = 0; floor < building.floors.size(); floor++) {
            if (!building.getWaitingPassengers(floor).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // Helper to format direction
    private String getDirectionString() {
        int dir = elevator.getDirection();
        if (dir == 1) return "UP";
        if (dir == -1) return "DOWN";
        return "IDLE";
    }

    // Print current building state
    public void printBuildingState() {
        System.out.println("\n--- Building State ---");
        for (int floor = building.floors.size() - 1; floor >= 0; floor--) {
            int waiting = building.getWaitingPassengers(floor).size();
            String elevatorHere = (elevator.getFloor() == floor) ? " [ELEVATOR]" : "";
            System.out.printf("Floor %2d: %d waiting%s%n", floor, waiting, elevatorHere);
        }
        System.out.println("---------------------\n");
    }

    // Main method with test scenarios
    public static void main(String[] args) {
        // Test 1: Simple up scenario
        System.out.println("TEST 1: Simple Up Movement");
        System.out.println("=============================");
        ElevatorSimulation sim1 = new ElevatorSimulation(10, 5);
        sim1.addRequest(0, 5);
        sim1.addRequest(2, 7);
        sim1.run(50);

        System.out.println("\n\n");

        // Test 2: Up then down
        System.out.println("TEST 2: Up Then Down");
        System.out.println("=============================");
        ElevatorSimulation sim2 = new ElevatorSimulation(10, 5);
        sim2.addRequest(0, 8);
        sim2.addRequest(3, 9);
        sim2.addRequest(8, 2);
        sim2.run(50);

        System.out.println("\n\n");

        // Test 3: Multiple passengers, mixed directions
        System.out.println("TEST 3: Complex Scenario");
        System.out.println("=============================");
        ElevatorSimulation sim3 = new ElevatorSimulation(15, 5);
        sim3.addRequest(0, 10);
        sim3.addRequest(2, 8);
        sim3.addRequest(5, 12);
        sim3.addRequest(10, 3);
        sim3.addRequest(8, 1);
        sim3.printBuildingState();
        sim3.run(100);

        System.out.println("\n\n");

        // Test 4: Capacity limit
        System.out.println("TEST 4: Capacity Test (6 passengers, 3 capacity)");
        System.out.println("=============================");
        ElevatorSimulation sim4 = new ElevatorSimulation(10, 3);
        sim4.addRequest(0, 5);
        sim4.addRequest(0, 6);
        sim4.addRequest(0, 7);
        sim4.addRequest(0, 8);
        sim4.addRequest(0, 9);
        sim4.run(50);
    }
}