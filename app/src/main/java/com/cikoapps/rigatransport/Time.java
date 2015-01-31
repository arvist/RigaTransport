package com.cikoapps.rigatransport;

/**
 * Created by arvis.taurenis on 1/30/2015.
 */
public class Time {
    int id;
    String time;
    int stopId;
    int hourOfDay;
    int minutesOfHour;
    int difference;

    public Time(int id, String time, int stopId) {
        this.id = id;
        this.time = time.trim();
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
