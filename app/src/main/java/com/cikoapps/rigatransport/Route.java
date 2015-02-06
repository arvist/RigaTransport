package com.cikoapps.rigatransport;


/**
 * Creation date 1/25/2015
 * -------------------------
 * Modified 2/1/2015 by Arvis code formatting
 */
class Route {

    private String name;
    private int number;
    private int id;
    private int type;

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
