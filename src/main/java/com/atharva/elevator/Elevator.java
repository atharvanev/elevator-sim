package com.atharva.elevator;
import java.util.PriorityQueue;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Elevator{
    private int floor;
    final private int capacity;
    private int current_capacity;
    private int direction;
    private PriorityQueue<Integer> up_requests;
    private PriorityQueue<Integer> down_requests;

    public Elevator() {
        this.floor = 0;
        this.capacity = 5;
        this.direction = 1;
    }

    public Elevator(int floor, int capacity, int direction) {
        this.floor = floor;
        this.capacity = capacity;
        this.direction = direction;
        this.current_capacity = 0;
        this.up_requests = new PriorityQueue<>();
        this.down_requests = new PriorityQueue<>(Collections.reverseOrder());
    }

    public void step(Building building) {
        if (direction == 0){
            find_new_request(building);
        } else{
            if (check_if_stop(building)) {
                arrive_at(floor, building);
            }
            if (direction !=0){
                move();
            }
        }
    }

    public void arrive_at(int floor,Building building) {
        unload();
        pickup(building);
        updateDirection();
    }

    private void unload(){
        if (direction == 1){
            while (!up_requests.isEmpty() && up_requests.peek() == floor){
                up_requests.poll();
                current_capacity--;
            }
        }

        else if (direction == -1){
            while (!down_requests.isEmpty() && down_requests.peek() == floor){
                down_requests.poll();
                current_capacity--;
            }
        }
    }


    private void pickup(Building building) {
        List<Request> waiting = building.getWaitingPassengers(floor);
        List<Request> toRemove = new ArrayList<>();

        for (Request request : waiting) {
            if (current_capacity >= capacity) break;

            int requestDirection = Integer.compare(request.getTo(), floor);

            if (requestDirection == direction || direction == 0) {
                if (request.getTo() > floor) {
                    up_requests.add(request.getTo());
                } else if (request.getTo() < floor) {
                    down_requests.add(request.getTo());
                }
                current_capacity++;
                toRemove.add(request);
            }
        }

        // Remove after iteration completes
        for (Request request : toRemove) {
            building.removePassenger(floor, request);
        }
    }

    private void updateDirection() {
        if (direction == 1) {
            if (up_requests.isEmpty()) {
                if (!down_requests.isEmpty()) {
                    direction = -1;
                } else {
                    direction = 0;
                }
            }
        } else if (direction == -1) {
            if (down_requests.isEmpty()) {
                if (!up_requests.isEmpty()) {
                    direction = 1;
                } else {
                    direction = 0;
                }
            }
        }
    }

    private void find_new_request (Building building){
        int nearest_floor = -1;
        int min_distance = Integer.MAX_VALUE;

        for(int f  = 0; f < building.floors.size(); f++){
            List<Request> waiting = building.getWaitingPassengers(f);

            if (!waiting.isEmpty()) {
                int distance = Math.abs(f - floor);

                // Find nearest floor with waiting passengers
                if (distance < min_distance) {
                    min_distance = distance;
                    nearest_floor = f;
                }
            }
        }
        if (nearest_floor != -1) {
            if (nearest_floor > floor) {
                direction = 1; // Go up
            } else if (nearest_floor < floor) {
                direction = -1; // Go down
            }
        }
    }



private boolean check_if_stop(Building building) {
    if (direction == 1 && !up_requests.isEmpty() && up_requests.peek() == floor) {
        return true;
    }
    if (direction == -1 && !down_requests.isEmpty() && down_requests.peek() == floor) {
        return true;
    }

    if (!isFull()) {
        List<Request> waiting = building.getWaitingPassengers(floor);
        for (Request request : waiting) {
            int requestDirection = Integer.compare(request.getTo(), floor);
            if (requestDirection == direction) {
                return true;
            }
        }
    }
    return false;
}

public void move(){
    floor+= direction;
}

// Getters
public int getFloor() {
    return floor;
}

public int getDirection() {
    return direction;
}

public int getCurrentCapacity() {
    return current_capacity;
}

public int getCapacity() {
    return capacity;
}

public boolean isEmpty() {
    return current_capacity == 0;
}

public boolean isFull() {
    return current_capacity >= capacity;
}

public boolean isIdle() {
    return direction == 0;
}

public boolean hasDestinations() {
    return !up_requests.isEmpty() || !down_requests.isEmpty();
}

}
