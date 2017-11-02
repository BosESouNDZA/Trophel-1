package com.example.bhurivatmontri.trophel.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bhurivatmontri.trophel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {

    private TextView profilenameTextView;
    private TextView emailTextView;
    private ImageView photoView;
    public Profile() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profilenameTextView = (TextView) view.findViewById(R.id.name_Profile);
        emailTextView = (TextView) view.findViewById(R.id.login_email);
        photoView = (ImageView) view.findViewById(R.id.img_Profile);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
        {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            Picasso.with(getApplicationContext()).load(photoUrl).resize(400, 400).centerCrop().into(photoView);
            profilenameTextView.setText(name);
            emailTextView.setText(email);
        }
        return view;
    }

}
