package com.example.gabriel.inventory_project.History;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gabriel on 28.02.18.
 */

public class Record {
    String time;
    String record;

    public Record() {}

    public Record(String record){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        this.time = simpleDateFormat.format(calendar.getTime()).toString();
        this.record = record;
    }

    public String getTime() {
        return time;
    }

    public String getRecord() {
        return record;
    }
}
