package com.example.gabriel.inventory_project.Inventory_pg.thing;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gabriel on 09.02.18.
 */

public class Thing {
    private String id;
    private String name;
    private String type;
    private String price;
    private String date_of_add;
    private String date_of_delete;
    private String id_image;

    public Thing(){

    }

    public Thing(String id, String name, String type, String price, int expiration_date) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.date_of_add = simpleDateFormat.format(calendar.getTime()).toString();
        calendar.add(Calendar.YEAR,expiration_date);
        this.date_of_delete = simpleDateFormat.format(calendar.getTime()).toString();
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getPrice() {return price;}

    public String getDate_of_add() {
        return date_of_add;
    }

    public String getDate_of_delete() {
        return date_of_delete;
    }

    public String getId_image() {
        return id_image;
    }

    public void setId_image(String id_image) {
        this.id_image = id_image;
    }
}
