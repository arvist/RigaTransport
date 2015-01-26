package com.cikoapps.rigatransport;

/**
 * Created by arvis on 15.20.1.
 */
public class Route {


    String name;
    int number;
    int id;
    int type;


    public Route(String name, int number, int id, int type) {
        this.name = name;
        this.number = number;
        this.id = id;
        this.type = type;
    }

    public String getName() {
        return name;

    }

    public int getNumber() {
        return number;

    }

    public int getId() {
        return id;

    }

    public int getType() {
        return type;
    }


}
