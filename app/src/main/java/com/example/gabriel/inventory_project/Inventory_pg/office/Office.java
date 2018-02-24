package com.example.gabriel.inventory_project.Inventory_pg.office;

/**
 * Created by gabriel on 09.02.18.
 */

public class Office {
    private String id;
    private String name;
    private String location;

    public Office(){}

    public Office(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
