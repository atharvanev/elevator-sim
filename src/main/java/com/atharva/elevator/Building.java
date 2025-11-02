package com.atharva.elevator;

import java.util.*;

public class Building {
    Map<Integer, List<Request>> floors = new HashMap<>();

    public Building(int to) {
        for (int i=0;i<to;i++) {
            floors.put(i, new ArrayList<>());
        }
    }

    public void removePassenger(int floor, Request request) {
        floors.get(floor).remove(request);
    }
    public void addPassenger(int floor, Request request) {
        if (floor >=0 && floor < floors.size()) {
            floors.get(floor).add(request);
        }
    }

    public List<Request> getWaitingPassengers(int floor) {
        return floors.get(floor);
    }
}