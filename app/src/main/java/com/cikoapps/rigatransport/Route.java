package com.cikoapps.rigatransport;

/**
 * Created by arvis on 15.20.1.
 */
public class Route {


    String start;
    String end;
    int number;

    public Route(String start, String end, int number){
        this.start=start;
        this.end = end;
        this.number = number;
    }
    public String getStart() {
        return this.start;
    }
    public String getEnd(){
        return this.end;
    }
    public int getNumber(){
        return this.number;
    }
}
