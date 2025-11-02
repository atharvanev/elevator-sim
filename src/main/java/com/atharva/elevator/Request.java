package com.atharva.elevator;
import  java.util.Comparator;
public class Request {
    private int from;
    private int to;
    private int capacity;

    public Request(int floor,int to,int capacity) {
        this.from = floor;
        this.to = to;
    }

    public int getFrom() { return from; }
    public int getTo() { return to; }
    public int getCapacity() { return capacity; }

    public static Comparator<Request> ascending = (r1,r2) -> r1.to - r2.to;
    public static Comparator<Request> descending = (r1,r2) -> r2.to - r1.to;
}
