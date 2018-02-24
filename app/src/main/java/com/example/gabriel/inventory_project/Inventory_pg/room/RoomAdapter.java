package com.example.gabriel.inventory_project.Inventory_pg.room;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gabriel.inventory_project.R;

import java.util.ArrayList;

/**
 * Created by gabriel on 09.02.18.
 */

public class RoomAdapter extends ArrayAdapter<Room> {
    public RoomAdapter(Context context, ArrayList<Room> rooms) {
        super(context, 0, rooms);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Room currentRoom = getItem(position);

        TextView nameTextView =listItemView.findViewById(R.id.name_object);

        nameTextView.setText("Room: "+currentRoom.getName());

        ImageView imageView =listItemView.findViewById(R.id.image);
        imageView.setVisibility(View.GONE);

        return listItemView;
    }
}

