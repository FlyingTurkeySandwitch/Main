package com.test1.videotest.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.test1.videotest.R;
import com.test1.videotest.adapter.HelperViewAdapter;


import de.hdodenhof.circleimageview.CircleImageView;


public class Profile extends Fragment {

    private TextView nameTv, descriptionTv, toolbarNameTv, statusTv, followCountTv, followersCountTv, postCountTv, currentPostTv, currentPosts, currentHelpTv, currentHelp;
    private CircleImageView profileImage;
    private Button followBtn;
    private ImageButton settingsBtn;
    private RecyclerView recyclerView;


    boolean isMyProfile = true;



    private FirebaseUser user;

    public Profile() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        if (isMyProfile){
            followBtn.setVisibility(View.GONE);
        }
        else {
            followBtn.setVisibility(View.VISIBLE);
        }

        loadBasicData();

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Settings Excluded from Taskr Version 1.0", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init(View view) {

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        assert getActivity() != null;
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        nameTv = view.findViewById(R.id.nameTv);
        descriptionTv = view.findViewById(R.id.actDescriptionTv);
        toolbarNameTv = view.findViewById(R.id.toolbarNameTV);
        profileImage = view.findViewById(R.id.profileImage);
        followBtn = view.findViewById(R.id.followBtn);
        recyclerView = view.findViewById(R.id.recyclerView);


        currentHelpTv = view.findViewById(R.id.textView);
        currentHelp = view.findViewById(R.id.currentTaskTv);

        currentPostTv = view.findViewById(R.id.currentPost);
        currentPosts = view.findViewById(R.id.postName);



        settingsBtn = view.findViewById(R.id.settingsBtn);




        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


    }

    private void loadBasicData() {

        DocumentReference userRef = FirebaseFirestore.getInstance().collection("Users")
                .document(user.getUid());

        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null)
                    return;

                assert value != null;
                if (value.exists()) {

                    String name = value.getString("name");
                    String description = value.getString("email");
//                    int followers = value.getLong("name").intValue();
//                    int following = value.getLong("name").intValue();
                    //vid 9 to implement

                    String profileURL = value.getString("profileImage");
                    String currTaskHelping = value.getString("description");
                    String currTaskPosting = value.getString("profileImage");


                    nameTv.setText(name);
                    toolbarNameTv.setText(name + "'s Profile");
                    descriptionTv.setText(description);

                    currentHelpTv.setVisibility(View.VISIBLE);
                    currentHelp.setVisibility(View.VISIBLE);
                    currentHelp.setText(currTaskHelping);


                    currentPostTv.setVisibility(View.VISIBLE);
                    currentPosts.setVisibility(View.VISIBLE);
                    currentPosts.setText(currTaskPosting);



                }
            }
        });


    }

}