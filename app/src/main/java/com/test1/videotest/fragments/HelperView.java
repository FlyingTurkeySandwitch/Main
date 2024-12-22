package com.test1.videotest.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.test1.videotest.R;
import com.test1.videotest.adapter.HelperViewAdapter;
import com.test1.videotest.model.PostModel;


import java.util.ArrayList;
import java.util.List;


public class HelperView extends Fragment {

    HelperViewAdapter adapter;
    private ImageButton sendBtn;
    private RecyclerView recyclerView;
    private List<PostModel> list;
    private FirebaseUser user;
    private Profile profile;



    public HelperView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_helperview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

//        reference = FirebaseFirestore.getInstance().collection("Posts").document(user.getUid());

        list = new ArrayList<>();
        adapter = new HelperViewAdapter(list, getContext(), profile);
        recyclerView.setAdapter(adapter);

        loadDataFromFirestore();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Messaging excluded from Taskr Version 1.0", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void init(View view) {

        Toolbar toolbar = view.findViewById(R.id.toolbar);

        if (getActivity() != null)
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);



        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sendBtn = view.findViewById(R.id.sendBtn);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

    }

    private void loadDataFromFirestore() {
        CollectionReference taskReference = FirebaseFirestore.getInstance().collection("Tasks");


        taskReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list.add(new PostModel(
                                        document.get("taskName").toString(),
                                        document.get("userName").toString(),
                                        "Timestamp",
                                        "",
                                        document.get("postID").toString(),
                                        "1234578",
                                        "Zip Code: " + document.get("locationEt").toString(),
                                        "Task Description: " + document.get("postDescriptionEt").toString(),
                                        document.get("priceEt").toString()));
                                Log.i("list-object added", document.get("taskName").toString() + ": " + document.get("priceEt").toString());
                                Log.d("doc-retrieve", document.getId() + " => " + document.getData());
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("doc-retrieve", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}