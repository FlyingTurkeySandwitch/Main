package com.test1.videotest.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.test1.videotest.GlideApp;
import com.test1.videotest.R;
import com.test1.videotest.fragments.Profile;
import com.test1.videotest.model.PostModel;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class HelperViewAdapter extends RecyclerView.Adapter<HelperViewAdapter.HelperHolder> {

    private List<PostModel> list;
    Context context;
    private Profile profile;
    private FirebaseFirestore db;
    private FirebaseAuth auth;


    public HelperViewAdapter(List<PostModel> list, Context context, Profile profile) {
        this.list = list;
        this.context = context;
        this.profile = profile;
    }

    @NonNull
    @Override
    public HelperHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_view, parent, false);
        return new HelperHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull HelperHolder holder, int position) {

        holder.postName.setText(list.get(position).getJobType());

        holder.usernameTv.setText(list.get(position).getUserName());

        holder.timeTv.setText(list.get(position).getTimestamp());

        holder.locationTv.setText(list.get(position).getLocation());

        holder.descriptionTv.setText(list.get(position).getDescription());

        holder.price.setText((list.get(position)).getPrice());

        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://videotest-74917.appspot.com/images/" + list.get(position).getPostImage() + ".jpg");
        Log.i("image sent to helperview", "gs://videotest-74917.appspot.com/images/" + list.get(position).getPostImage());
        Random random = new Random();

        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

        Glide.with(context.getApplicationContext())
                .load(list.get(position).getProfileImage())
                .placeholder(R.drawable.ic_blankuser)
                .timeout(6500)
                .into(holder.profileImage);

        GlideApp.with(context.getApplicationContext())
                .load(storageReference)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                        Log.e("ImageViewError", "Error loading image", e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.imageView);

        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("XXX", "task accepted");
                Log.i("XXX-task name", list.get(position).getJobType());

                Toast.makeText(v.getContext(), "Task accepted. View current accepted task on Profile", Toast.LENGTH_LONG).show();

                String taskName = list.get(position).getJobType();
                uploadTaskAcceptedData(taskName);

            }
        });

        holder.xoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("XXX", "task rejected");
                Log.i("XXX-task name", list.get(position).getJobType());

                Toast.makeText(v.getContext(), "Task removed from current tasks. View active tasks on Profile", Toast.LENGTH_LONG).show();

                String taskName = list.get(position).getJobType();
                removeTaskAccepted(taskName);

            }
        });

        holder.bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Bookmark feature not active in Version 1.0", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class HelperHolder extends RecyclerView.ViewHolder {

        private CircleImageView profileImage;
        private TextView postName, usernameTv, timeTv, locationTv, descriptionTv, price;
        private ImageView imageView;
        private ImageButton acceptBtn, bookmarkBtn, xoutBtn;
        public HelperHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            imageView = itemView.findViewById(R.id.imageView);
            usernameTv = itemView.findViewById(R.id.usernameTv);
            postName = itemView.findViewById(R.id.taskName);
            timeTv = itemView.findViewById(R.id.timeTv);
            locationTv = itemView.findViewById(R.id.locationTv);
            descriptionTv = itemView.findViewById(R.id.descTv);

            acceptBtn = itemView.findViewById(R.id.acceptPostBtn);
            bookmarkBtn = itemView.findViewById(R.id.bookmarkBtn);
            xoutBtn = itemView.findViewById(R.id.xoutBtn);
            price = itemView.findViewById(R.id.priceOffer);

        }
    }

    private void uploadTaskAcceptedData(String taskId) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Users").document(auth.getUid());

        //Uses "description" field because it is currently unused, future version would require different field name
        documentReference.update("description", taskId);
    }

    private void removeTaskAccepted(String taskId) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Users").document(auth.getUid());

        //check if taskId matches "description" field

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Read the data of a field
                        String fieldValue = document.getString("description");
                        if (fieldValue != taskId) {
                            documentReference.update("description", "");
                        }
                        Log.d("Firestore-HVA", "Field value updated");
                    } else {
                        Log.d("Firestore-HVA", "No such document");
                    }
                } else {
                    Log.d("Firestore-HVA", "get failed with ", task.getException());
                }
            }
        });
        //if it does match, then change "description" field to ""
        //if it doesn't match, then toast error
    }

}
