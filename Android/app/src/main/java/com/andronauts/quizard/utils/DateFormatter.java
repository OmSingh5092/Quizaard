package com.andronauts.quizard.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {
    Long timestamp;
    SimpleDateFormat simpleDateFormat;
    Date date;

    public DateFormatter(Long timestamp){
        this.timestamp = timestamp;
        date = new Date(this.timestamp);
    }

    public DateFormatter(String timestamp){
        this.timestamp = Long.valueOf(timestamp);
        date = new Date(this.timestamp);
    }



    public String getTime(){
        simpleDateFormat = new SimpleDateFormat("hh:mm:aa");
        return simpleDateFormat.format(date);
    }

    public String getDate(){
        simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        return simpleDateFormat.format(date);
    }
}
