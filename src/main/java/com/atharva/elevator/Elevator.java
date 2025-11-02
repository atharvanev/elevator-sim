package com.atharva.elevator;
import java.util.PriorityQueue;
import java.util.Collections;
import com.atharva.elevator.Request;

public class Elevator {
    private int floor;
    private int capacity;
    private int direction;
    private PriorityQueue<Request> upRequests = new PriorityQueue<>(Request.descending); // min-heap
    private PriorityQueue<Request> downRequests = new PriorityQueue<>(Request.ascending);

    public Elevator() {
        this.floor = 0;
        this.capacity = 5;
        this.direction = 1;
    }

    public Elevator(int floor, int capacity, int direction) {
        this.floor = floor;
        this.capacity = capacity;
        this.direction = direction;
    }

    public void moveFloor(){
        this.floor+= direction;
    }

    public void scan(Request[] requests){

        //unloading passengers
        if (this.direction > 0){
            while( !upRequests.isEmpty() && (upRequests.peek().getTo() == floor)){
                upRequests.poll();
            }
            if(upRequests.isEmpty() && !downRequests.isEmpty()){
                direction = -1;
            }
        }
        else {
            while( !downRequests.isEmpty() && (downRequests.peek().getTo() == floor)){
                downRequests.poll();
            }
            if(downRequests.isEmpty() && !upRequests.isEmpty()){
                direction = 1;
            }
        }

        //if no requests on this floor
        if (requests.length == 0) { return;}

        //if new direction needs to be set
        if (!upRequests.isEmpty() && !downRequests.isEmpty()){
            int i = 0;

            while(i < requests.length && requests[i].getTo() == floor) {
                i++;
            }

            if (i >= requests.length ){return;}

            if (requests[i].getTo() > floor) {
                direction = 1;
            }
            else{
                direction = -1;
            }
        }

        for  (Request request : requests) {
            if(request.getTo() > floor) {
                upRequests.add(request);
            }
            else if (request.getTo() < floor) {
                downRequests.add(request);
            }
        }
    }
}
