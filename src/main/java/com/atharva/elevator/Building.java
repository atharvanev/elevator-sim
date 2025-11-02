package com.atharva.elevator;

import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Building {
    Map<Integer, Queue<Request>> floors = new HashMap<>();

    public Building(int to) {
        for (int i=0;i<to;i++) {
            floors.put(i, new LinkedList<>());
        }
    }

    public void removePassenger(int floor) {
        if (!floors.get(floor).isEmpty()){
            floors.get(floor).poll();
        }
    }
    public void addPassenger(int floor, Request request) {
        if (floors.containsKey(floor)){
            floors.get(floor).add(request);
        }
    }
}
