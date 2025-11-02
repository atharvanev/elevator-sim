package com.atharva.elevator;

import java.util.ArrayList;
import java.util.List;

public class ElevatorSimulation {
    private Building building;
    private Elevator elevator;
    private int currentStep;
    private List<ScheduledRequest> scheduledRequests;

    public ElevatorSimulation(int numFloors, int elevatorCapacity) {
        this.building = new Building(numFloors);
        this.elevator = new Elevator(0, elevatorCapacity, 0);
        this.currentStep = 0;
        this.scheduledRequests = new ArrayList<>();
    }

    // Inner class for scheduled requests
    private static class ScheduledRequest {
        int fromFloor;
        int toFloor;
        int atStep;

        ScheduledRequest(int from, int to, int step) {
            this.fromFloor = from;
            this.toFloor = to;
            this.atStep = step;
        }
    }

    // Add a passenger request with a scheduled time
    public void addRequest(int fromFloor, int toFloor, int atStep) {
        scheduledRequests.add(new ScheduledRequest(fromFloor, toFloor, atStep));
    }

    // Process any scheduled requests that should happen this step
    private void processScheduledRequests() {
        List<ScheduledRequest> toRemove = new ArrayList<>();

        for (ScheduledRequest sr : scheduledRequests) {
            if (sr.atStep == currentStep) {
                building.addPassenger(sr.fromFloor, new Request(sr.fromFloor, sr.toFloor));
                toRemove.add(sr);
            }
        }

        scheduledRequests.removeAll(toRemove);
    }

    public void step() {
        // Process any scheduled requests for this step
        processScheduledRequests();

        // Execute elevator logic
        elevator.step(building);
        currentStep++;

        // Display the building AFTER the step
        //moveCursorToTop();
        printBuilding();

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void run(int maxSteps) {
        System.out.println("=== Starting Elevator Simulation ===\n");
        System.out.println("Legend: [3/5↑] = Elevator (3 passengers, capacity 5, going up)");
        System.out.println("        (2)    = 2 people waiting on floor");
        System.out.println("        ↑ = Up | ↓ = Down | • = Idle\n");
        System.out.println("Animation will start in 2 seconds...\n");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        while (currentStep < maxSteps) {
            step();

            if (isSimulationComplete()) {
                // Show final state one more time to display idle elevator
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                System.out.println("\n\n=== All passengers delivered! ===");
                System.out.println("Total steps: " + currentStep);
                break;
            }
        }

        if (currentStep >= maxSteps) {
            System.out.println("\n\n=== Max steps reached ===");
        }
    }

    private boolean isSimulationComplete() {
        return elevator.isEmpty() &&
                elevator.isIdle() &&
                !hasWaitingPassengers();
    }

    private boolean hasWaitingPassengers() {
        for (int floor = 0; floor < building.floors.size(); floor++) {
            if (!building.getWaitingPassengers(floor).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void printBuilding() {
        int numFloors = building.floors.size();
        int elevatorFloor = elevator.getFloor();
        int load = elevator.getCurrentCapacity();
        int capacity = elevator.getCapacity();
        String direction = getDirectionSymbol();

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║           ELEVATOR BUILDING            ║");
        System.out.println("╠════════════════════════════════════════╣");

        // Print floors from top to bottom
        for (int floor = numFloors - 1; floor >= 0; floor--) {
            int waiting = building.getWaitingPassengers(floor).isEmpty() ?
                    0 : building.getWaitingPassengers(floor).size();

            String floorNum = String.format("%2d", floor);
            String waitingStr = waiting > 0 ? String.format("(%d)", waiting) : "   ";

            if (floor == elevatorFloor) {
                String elevatorDisplay = String.format("[%d/%d%s]", load, capacity, direction);
                System.out.printf("║ %s │ %-8s %-24s ║%n", floorNum, waitingStr, elevatorDisplay);
            } else {
                System.out.printf("║ %s │ %-8s %-24s ║%n", floorNum, waitingStr, "");
            }
        }

        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
        System.out.printf("Step: %d | Direction: %s | Load: %d/%d%n",
                currentStep, getDirectionString(), load, capacity);
    }

    private String getDirectionSymbol() {
        int dir = elevator.getDirection();
        if (dir == 1) return "↑";
        if (dir == -1) return "↓";
        return "•";
    }

    private String getDirectionString() {
        int dir = elevator.getDirection();
        if (dir == 1) return "UP  ";
        if (dir == -1) return "DOWN";
        return "IDLE";
    }

    public static void main(String[] args) {
        // Test 1: Simple up scenario
        System.out.println("TEST 1: Simple Up Movement");
        System.out.println("=============================");
        ElevatorSimulation sim1 = new ElevatorSimulation(10, 5);
        sim1.addRequest(0, 5, 0);  // Immediate request
        sim1.addRequest(2, 7, 0);  // Immediate request
        sim1.run(50);

        System.out.println("\n\nPress Enter for next test...");
        try { System.in.read(); } catch (Exception e) {}

        // Test 2: Requests arriving during operation
        System.out.println("\n\nTEST 2: Mid-Travel Requests");
        System.out.println("=============================");
        ElevatorSimulation sim2 = new ElevatorSimulation(10, 5);
        sim2.addRequest(0, 8, 0);   // Start immediately
        sim2.addRequest(5, 9, 5);   // New passenger appears at step 5
        sim2.addRequest(7, 2, 10);  // New passenger appears at step 10
        sim2.run(50);

        System.out.println("\n\nPress Enter for next test...");
        try { System.in.read(); } catch (Exception e) {}

        // Test 3: Multiple passengers at different times
        System.out.println("\n\nTEST 3: Continuous Arrivals");
        System.out.println("=============================");
        ElevatorSimulation sim3 = new ElevatorSimulation(15, 5);
        sim3.addRequest(0, 10, 0);
        sim3.addRequest(2, 8, 3);
        sim3.addRequest(5, 12, 7);
        sim3.addRequest(10, 3, 12);
        sim3.addRequest(8, 1, 15);
        sim3.run(100);

        System.out.println("\n\nPress Enter for next test...");
        try { System.in.read(); } catch (Exception e) {}

        // Test 4: Rush hour simulation
        System.out.println("\n\nTEST 4: Rush Hour (Many Requests)");
        System.out.println("=============================");
        ElevatorSimulation sim4 = new ElevatorSimulation(10, 3);
        sim4.addRequest(0, 5, 0);
        sim4.addRequest(0, 6, 2);
        sim4.addRequest(0, 7, 4);
        sim4.addRequest(3, 8, 6);
        sim4.addRequest(2, 9, 8);
        sim4.addRequest(7, 1, 15);
        sim4.run(80);
    }
}