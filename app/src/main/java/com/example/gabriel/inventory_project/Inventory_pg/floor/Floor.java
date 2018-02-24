package com.example.gabriel.inventory_project.Inventory_pg.floor;

/**
 * Created by gabriel on 09.02.18.
 */

public class Floor {
    private String id;
    private String name;
    public  Floor(){
    }
    public Floor(String id, String name) {
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
