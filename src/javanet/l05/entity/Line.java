package javanet.l05.entity;

import java.util.ArrayList;

public class Line {

    private ArrayList<String> stops;
    private int code;

    public Line() {
    }

    public Line(ArrayList<String> stops, int code) {
        this.stops = stops;
        this.code = code;
    }

    public ArrayList<String> getStops() {
        return stops;
    }

    public void setStops(ArrayList<String> stops) {
        this.stops = stops;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int size() {
        return stops.size();
    }
}
