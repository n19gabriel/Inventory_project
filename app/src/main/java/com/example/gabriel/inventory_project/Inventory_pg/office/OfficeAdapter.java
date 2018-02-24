package com.example.gabriel.inventory_project.Inventory_pg.office;

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

public class OfficeAdapter extends ArrayAdapter<Office> {

    public OfficeAdapter(Context context, ArrayList<Office> offices) {
        super(context, 0, offices);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Office currentWord = getItem(position);

        TextView nameTextView = listItemView.findViewById(R.id.name_object);

        nameTextView.setText(currentWord.getName());

        TextView locationTextView = listItemView.findViewById(R.id.info_object);

        locationTextView.setText(currentWord.getLocation());

        ImageView imageView = listItemView.findViewById(R.id.image);
        imageView.setVisibility(View.GONE);

        return listItemView;
    }
}
