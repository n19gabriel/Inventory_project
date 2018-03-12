package com.example.gabriel.inventory_project.Inventory_pg.thing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gabriel.inventory_project.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;

/**
 * Created by gabriel on 10.02.18.
 */

public class ThingAdapter extends ArrayAdapter<Thing> {
    Context context;
    private StorageReference mStorageRef;
    public ThingAdapter(Context context, ArrayList<Thing> things) {
        super(context, 0, things);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_items, parent, false);
        }

        Thing currentThing = getItem(position);

        TextView nameTextView = listItemView.findViewById(R.id.name_object);

        nameTextView.setText(currentThing.getName());

        TextView locationTextView = listItemView.findViewById(R.id.info_object);

        locationTextView.setText(currentThing.getType());

        ImageView imageView = listItemView.findViewById(R.id.image);
        // Check if an image is provided for this word or not

        if(currentThing.getId_image()!=null) {
            mStorageRef = FirebaseStorage.getInstance().getReference().child(currentThing.getId_image());
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(mStorageRef)
                    .into(imageView);
            // Make sure the view is visible
            imageView.setVisibility(View.VISIBLE);
        }else {
            imageView.setVisibility(View.GONE);
        }

        return listItemView;
    }
}
