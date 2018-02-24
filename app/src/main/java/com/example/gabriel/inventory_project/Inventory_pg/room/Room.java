package com.example.gabriel.inventory_project.Inventory_pg.room;

/**
 * Created by gabriel on 09.02.18.
 */

public class Room {
    private String id;
    private String name;

    public Room(){}

    public Room(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}