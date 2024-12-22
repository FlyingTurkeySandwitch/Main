package com.test1.videotest.fragments;

import static com.test1.videotest.fragments.CreateAccountFragment.EMAIL_REGEX;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.test1.videotest.FragmentReplacerActivity;
import com.test1.videotest.MainActivity;
import com.test1.videotest.R;


public class LoginFragment extends Fragment {

    private EditText emailEt, passEt;
    private TextView signUpTv, forgotPassTv;
    private Button loginBtn, googleSignInBtn;
    private ProgressBar progressBar;

    private static final int RC_SIGN_IN = 1;

    private FirebaseAuth auth;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        auth = FirebaseAuth.getInstance();
        //Return video 5 to implement google sign in
        clickListener();
    }

    private void init(View view) {
        emailEt = view.findViewById(R.id.emailET);
        passEt = view.findViewById(R.id.passET);
        loginBtn = view.findViewById(R.id.loginBtn);
        //googleSignInBtn = view.findViewById(R.id.googleSignInBtn);
        signUpTv = view.findViewById(R.id.signUpTV);
        forgotPassTv = view.findViewById(R.id.forgotTV);
        progressBar = view.findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();
    }

    private void clickListener() {
        loginBtn.setOnClickListener(v -> {
            String email = emailEt.getText().toString();
            String password = passEt.getText().toString();

            if (email.isEmpty() || !email.matches(EMAIL_REGEX)) {
                emailEt.setError("Input valid email");
                return;
            }

            if (password.isEmpty() || password.length() < 8) {
                passEt.setError("Input 8 digit password");
                return;
            }
            progressBar.setVisibility(View.VISIBLE);

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            FirebaseUser user = auth.getCurrentUser();

                            assert user != null;
                            if (!user.isEmailVerified()) {
                                Toast.makeText(getContext(), "Please verify your email", Toast.LENGTH_SHORT).show();
                            }

                            sendUserToMainActivity();

                        } else {
                            String exception = "LoginFragment Error: " + task.getException().getMessage();
                            Toast.makeText(getContext(), exception, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.VISIBLE);

                        }
                    });
        });

        signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentReplacerActivity) getActivity()).setFragment(new CreateAccountFragment());
            }
        });
    }

    private void sendUserToMainActivity() {

        if (getActivity() == null) {
            return;
        }

        progressBar.setVisibility(View.GONE);
        startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
        getActivity().finish();
    }
}