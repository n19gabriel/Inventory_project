package com.example.gabriel.inventory_project.Inventory_pg.thing.Info;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gabriel.inventory_project.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {
    private ImageView image_things;
    private TextView TV_name;
    private TextView TV_type;
    private TextView TV_price;
    private TextView TV_path;
    private TextView TV_date_of_add;
    private TextView TV_date_of_delete;
    private Button delete_thing;
    private Button refract_thing;

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
    private StorageReference mStorageRef;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public InfoFragment() {
        // Required empty public constructor
    }

    public void getItem(String id_Office,String name_Office,String id_Floor, String name_Floor,String id_Room,
                        String name_Room,String id_Thing,String name_Thing,String type_Thing,
                        String price_Thing,String date_of_add_Thing,String date_of_delete_Thing,String id_image){
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
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference(userId);
        View v = inflater.inflate(R.layout.fragment_info,null);
        image_things = v.findViewById(R.id.image_things);
        if(id_image!=null) {
            mStorageRef = FirebaseStorage.getInstance().getReference().child(id_image);
            Glide.with(InfoFragment.this)
                    .using(new FirebaseImageLoader())
                    .load(mStorageRef)
                    .into(image_things);
        }else {
            image_things.setVisibility(View.GONE);
        }

        TV_name = v.findViewById(R.id.TV_name);
        TV_name.setText("Name: "+name_Thing);
        TV_type = v.findViewById(R.id.TV_type);
        TV_type.setText("Type: "+type_Thing);
        TV_price = v.findViewById(R.id.TV_price);
        TV_price.setText("Price: "+price_Thing+"$");
        TV_path = v.findViewById(R.id.TV_path);
        TV_path.setText("Path: "+name_Office+"/"+name_Floor+"/"+name_Room+"/"+name_Thing);
        TV_date_of_add = v.findViewById(R.id.TV_date_of_add);
        TV_date_of_add.setText("Date of add: "+date_of_add_Thing);
        TV_date_of_delete = v.findViewById(R.id.TV_date_of_delete);
        TV_date_of_delete.setText("Date of delete: "+date_of_delete_Thing);

        delete_thing = v.findViewById(R.id.delete_thing);
        refract_thing = v.findViewById(R.id.refract_thing);
        // Inflate the layout for this fragment
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
