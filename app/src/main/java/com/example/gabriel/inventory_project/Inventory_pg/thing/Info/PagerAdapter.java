package com.example.gabriel.inventory_project.Inventory_pg.thing.Info;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

/**
 * Created by gabriel on 07.03.18.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    HistoryFragment historyFragment;
    private String id_Office;
    private String name_Office;
    private String id_Floor;
    private String name_Floor;
    private String id_Room;
    private String name_Room;
    private String id_Thing;
    private String name_Thing;
    private String type_Thing;
    private String price_Thing;
    private String date_of_add_Thing;
    private String date_of_delete_Thing;
    private String id_image;
    private ArrayList<String > dates;

    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs,
                        String id_Office,String name_Office,String id_Floor, String name_Floor,String id_Room,
                        String name_Room,String id_Thing,String name_Thing,String type_Thing,
                        String price_Thing,String date_of_add_Thing,String date_of_delete_Thing,String id_image)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;

        this.id_Office = id_Office;
        this.name_Office = name_Office;
        this.id_Floor = id_Floor;
        this.name_Floor = name_Floor;
        this.id_Room = id_Room;
        this.name_Room = name_Room;
        this.id_Thing = id_Thing;
        this.name_Thing = name_Thing;
        this.type_Thing = type_Thing;
        this.price_Thing = price_Thing;
        this.date_of_add_Thing= date_of_add_Thing;
        this.date_of_delete_Thing =date_of_delete_Thing;
        this.id_image =id_image;

        historyFragment = new HistoryFragment();

    }
    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                InfoFragment infoFragment = new InfoFragment();
                infoFragment.setItem(id_Office,name_Office,id_Floor,name_Floor,id_Room,name_Room,id_Thing,name_Thing,type_Thing,
                        price_Thing,date_of_add_Thing,date_of_delete_Thing,id_image);
                return infoFragment;
            case 1:
                historyFragment.setItem(id_Office,id_Floor,id_Room,id_Thing);
                return  historyFragment;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
