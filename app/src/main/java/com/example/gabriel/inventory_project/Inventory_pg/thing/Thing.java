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
    private String id_Office;
    private String name_Office;
    private String id_Floor;
    private String name_Floor;
    private String id_Room;
    private String name_Room;
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

    public Thing(String id, String name, String type, String price, String date_of_add, String date_of_delete, String id_image) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.date_of_add = date_of_add;
        this.date_of_delete = date_of_delete;
        this.id_image = id_image;
    }
    public Thing(String id, String name, String type, String price, String date_of_add, String date_of_delete) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.date_of_add = date_of_add;
        this.date_of_delete = date_of_delete;
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

    public void setPuth(String id_Office, String name_Office, String id_Floor, String name_Floor,
                        String id_Room, String name_Room){
        this.id_Office= id_Office;
        this.name_Office = name_Office;
        this.id_Floor =id_Floor;
        this.name_Floor = name_Floor;
        this.id_Room = id_Room;
        this.name_Room = name_Room;
    }

    public String getId_Office() {
        return id_Office;
    }

    public String getName_Office() {
        return name_Office;
    }

    public String getId_Floor() {
        return id_Floor;
    }

    public String getName_Floor() {
        return name_Floor;
    }

    public String getId_Room() {
        return id_Room;
    }

    public String getName_Room() {
        return name_Room;
    }
}
