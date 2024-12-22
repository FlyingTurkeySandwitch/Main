package com.test1.videotest.model;

import android.util.Log;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

import io.grpc.internal.DnsNameResolver;

public class PostModel {

    private String jobType, userName, timestamp, profileImage, postImage, uid, location, descriptionET, taskName, price;



    public PostModel () {
    }

    public PostModel(String jobType, String userName, String timestamp, String profileImage, String postImage, String uid, String location, String descriptionET, String price) {
        this.jobType = jobType;
        this.userName = userName;
        this.timestamp = timestamp;
        this.profileImage = profileImage;
        this.postImage = postImage;
        this.uid = uid;
        this.location = location;
        this.descriptionET = descriptionET;
        this.price = price;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public void setPrice(String price)
    {
        this.price = price;
        Log.i("XXX", "Price set to: "+ price);
    }

    public String getPrice() {
        return this.price;
    }
    public void setDescriptionET(String description)
    {
        this.descriptionET = description;
        Log.i("XXX", "description set");
    }

    public String getDescription() {
        return descriptionET;
    }
    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
        Log.i("XXX", "taskName set to: "+taskName);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
