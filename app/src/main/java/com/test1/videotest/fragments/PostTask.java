package com.test1.videotest.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import android.graphics.drawable.BitmapDrawable;
import com.test1.videotest.R;


public class PostTask extends Fragment {

    private EditText descriptionET, taskName, location, price;
    private ImageView imageView;
    private ImageButton sendBtn;
    private Button postTask;
    private static final int Request_Camera = 1;
    private FirebaseFirestore db;
    private MediaPlayer mediaPlayer;
    private FirebaseAuth auth;
    private String timestamp;



    public PostTask() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }
    //Writing a method that will clear the text boxes after putting in the task data. Method gets
    // called when we're creating our button.
    private void clearTextFields() {
        taskName.setText("");
        location.setText("");
        descriptionET.setText("");
        price.setText("");
        imageView.setImageDrawable(null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        //Old
        db = FirebaseFirestore.getInstance(); // Initialize Firestore
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.congrats);


        postTask.setOnClickListener(v -> {
            if (validateInput()) {
                String tname = saveTaskToFirestore();
                uploadTaskPostedData(tname);
                mediaPlayer.start();
                showPopupWindow(postTask);
                clearTextFields(); // method gets called right here.
                //Send task to profile

            } else {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        Button imageButton = view.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, Request_Camera);
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Messaging excluded from Taskr Version 1.0", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init(View view) {

        taskName = view.findViewById(R.id.taskName);
        location = view.findViewById(R.id.locationEt);
        imageView = view.findViewById(R.id.postImageView);
        price = view.findViewById(R.id.priceEt);
        descriptionET = view.findViewById(R.id.postDescriptionEt);
        auth = FirebaseAuth.getInstance();
        sendBtn = view.findViewById(R.id.sendBtn);
        postTask = view.findViewById(R.id.postTask);

    }



    private boolean validateInput() {
        String taskNameInput = taskName.getText().toString();
        String locationInput = location.getText().toString();
        String priceInput = price.getText().toString();

        // Regular expression to check if task name contains special characters
        String specialCharacters = "[!@#$%^&*]+";
        if (taskNameInput.matches(".*" + specialCharacters + ".*")) {
            taskName.setError("Task name should not contain special characters like !@#$%^&*");
            return false;
        }

        // Check if location has more than 5 digits
        if (locationInput.length() > 5) {
            location.setError("Location cannot have more than 5 digits.");
            return false;
        }
        // Check if price has more than 4 digits
        if (priceInput.length() > 4) {
            price.setError("Price cannot have more than 4 digits.");
            return false;
        }

        return !taskNameInput.isEmpty() &&
                !locationInput.isEmpty() &&
                !descriptionET.getText().toString().isEmpty() &&
                !price.getText().toString().isEmpty();
    }

    private String saveTaskToFirestore() {
        if (!validateInput()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return "";
        }
        Map<String, Object> task = new HashMap<>();
        FirebaseUser user = auth.getCurrentUser();

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
                    task.put("userName", name);
                }
            }
        });

        task.put("taskName", taskName.getText().toString());
        task.put("locationEt", location.getText().toString());
        task.put("postDescriptionEt", descriptionET.getText().toString());
        task.put("priceEt", price.getText().toString());
        task.put("userID", user.getUid());
        //task.put("userName", user.getDisplayName());
        String postID = UUID.randomUUID().toString();
        task.put("postID", postID);

        if (imageView.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageData = baos.toByteArray();

            String imageName = "images/" + postID + ".jpg";
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(imageName);
            storageRef.putBytes(imageData)
                    .addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        task.put("imageUrl", imageUrl);
                        uploadTaskData(task);
                    }).addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to get image URL: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to upload image: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        } else {
            uploadTaskData(task);
        }
        return taskName.getText().toString();
    }

    private void uploadTaskData(Map<String, Object> taskData) {
        String postID = taskData.get("postID").toString();
        db.collection("Tasks")
                .document(postID)
                .set(taskData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Task posted successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to post task: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Request_Camera && resultCode == getActivity().RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(image);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void showPopupWindow(View view) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });
    }

    private void uploadTaskPostedData(String taskId) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Users").document(auth.getUid());

        //Uses currently unused profileImage field since fields cannot be updated after initiation of Firebase
        //For future version would need to change to use a "currentPostings" field
        documentReference.update("profileImage", taskId);
    }
}
