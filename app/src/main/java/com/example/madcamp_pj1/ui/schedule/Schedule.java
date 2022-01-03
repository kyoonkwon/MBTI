package com.example.madcamp_pj1.ui.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Schedule implements Comparable<Schedule> {

    String name;
    // hour * 60 + min
    Date startTime;
    Date endTime;
    boolean alarm;

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public int compareTo(Schedule schedule){
        return sdf.format(startTime).compareTo(schedule.getStartTimeAsString());

    }

    public Schedule(String name, Date startTime, Date endTime, boolean alarm){
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.alarm = alarm;
    }

    public String getName() {
        return name;
    }

    public String getStartTimeAsString() {
        return sdf.format(startTime);
    }

    public String getEndTimeAsString() {
        return sdf.format(endTime);
    }

    public boolean getAlarm(){
        return alarm;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }
}
