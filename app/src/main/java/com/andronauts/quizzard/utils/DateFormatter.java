package com.andronauts.quizzard.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {
    Long timestamp;
    SimpleDateFormat simpleDateFormat;
    Date date;
    Long startTimeStamp, endTimeStamp;

    public DateFormatter(Long timestamp){
        this.timestamp = timestamp;
        date = new Date(this.timestamp);
    }

    public DateFormatter(String timestamp){
        this.timestamp = Long.valueOf(timestamp);
        date = new Date(this.timestamp);
    }

    public DateFormatter(String start, String end){
        this.startTimeStamp = Long.valueOf(start);
        this .endTimeStamp = Long.valueOf(end);

    }



    public String getTime(){
        simpleDateFormat = new SimpleDateFormat("hh:mm:aa");
        return simpleDateFormat.format(date);
    }

    public String getDate(){
        simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        return simpleDateFormat.format(date);
    }

    public String getDateAndTime(){
        simpleDateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
        return simpleDateFormat.format(date);
    }

    public long getDurationMinutes(){
        return (endTimeStamp-startTimeStamp)/60000;
    }

    public long getTimeStamp(){
        return timestamp;
    }
}
