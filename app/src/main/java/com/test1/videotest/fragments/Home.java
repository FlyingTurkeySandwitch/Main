package com.test1.videotest.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.test1.videotest.R;

//"#014426"
public class Home extends Fragment {
    private TextView descriptionTv, helperTv, posterTv;
    private ImageButton sendBtn;

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        chooseUseType();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Messaging excluded from Taskr Version 1.0", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void init(View view) {
        descriptionTv = view.findViewById(R.id.useTypeDescriptionTv);
        helperTv = view.findViewById(R.id.helperTv);
        posterTv = view.findViewById(R.id.posterTv);

        sendBtn = view.findViewById(R.id.sendBtn);

    }

    private void chooseUseType() {
        helperTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = getResources().getString(R.string.helper_description);
                descriptionTv.setText(text);
            }
        });
        posterTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = getResources().getString(R.string.poster_description);
                descriptionTv.setText(text);
            }
        });
    }
}