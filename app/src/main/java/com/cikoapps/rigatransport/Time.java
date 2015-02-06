package com.cikoapps.rigatransport;

/**
 * Creation date 1/30/2015
 * -------------------------
 * Modified 2/1/2015 by Arvis code formatting
 */

class Time {
    private int id;
    private String time;
    private boolean isStandart;
    private int stopId;
    private int hourOfDay;
    private int minutesOfHour;
    private int difference;

    public Time(int id, String time, int stopId, boolean isStandart) {
        this.id = id;
        this.time = time.trim();
        this.stopId = stopId;
        this.isStandart = isStandart;
        setHourOfDay();
        setMinutesOfHour();
    }

    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public int getStopId() {
        return stopId;
    }

    public boolean getIsStandart() {
        return this.isStandart;
    }

    public void setIsStandart(boolean s) {
        this.isStandart = s;
    }

    public void setHourOfDay() {
        String hour = time.substring(0, time.length() - 3);
        this.hourOfDay = Integer.parseInt(hour);
    }

    public void setMinutesOfHour() {
        String minutes = time.substring(time.length() - 2, time.length());
        this.minutesOfHour = Integer.parseInt(minutes);
    }

    public void setDifference(int diff) {
        this.difference = diff;
    }

    public int getDifference() {
        return this.difference;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public int getMinutesOfHour() {
        return minutesOfHour;
    }
}
