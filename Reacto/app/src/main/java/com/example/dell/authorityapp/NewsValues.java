package com.example.dell.authorityapp;

import android.graphics.Bitmap;

/**
 * Created by Dell on 15/02/2018.
 */

public class NewsValues {
    Bitmap bitmap;
    String imageUrl;
    String longitude;
    String latitude;
    String type;
    String timestamp;
    String userId;
    String title;
    String rating;
    String deviceId;
    String issueDate;
    String pincode;
    String location;
    String priority;
    String photoUrl;

    public String getJabberId() {
        return jabberId;
    }

    String emailId;
    String AJabberId;
    String jabberId;
    String tokenId;
    String proposedDate;
    String timelineStatus;
    String description;

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getAJabberId() {
        return AJabberId;
    }

    public void setJabberId(String jabberId) {
        AJabberId = jabberId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getProposedDate() {
        return proposedDate;
    }

    public void setProposedDate(String proposedDate) {
        this.proposedDate = proposedDate;
    }

    public String getTimelineStatus() {
        return timelineStatus;
    }

    public void setTimelineStatus(String timlineStatus) {
        this.timelineStatus = timelineStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NewsValues(String latitude, String longitude, String userId, String title, String type, String rating, String timestamp, String imageUrl, String deviceId, String issueDate, String pincode, String location, String priority, String photoUrl, String emailId, String AJabberId, String jabberId, String tokenId, String proposedDate, String timelineStatus, String description, Bitmap bitmap) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
        this.title = title;
        this.type = type;
        this.rating = rating;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
        this.bitmap = bitmap;

        this.deviceId=deviceId;
        this.issueDate=issueDate;
        this.pincode=pincode;
        this.location=location;
        this.priority=priority;
        this.photoUrl=photoUrl;
        this.emailId=emailId;
        this.AJabberId=AJabberId;
        this.jabberId=jabberId;
        this.tokenId=tokenId;
        this.proposedDate=proposedDate;
        this.timelineStatus=timelineStatus;
        this.description=description;
    }

    public void setAJabberId(String AJabberId) {
        this.AJabberId = AJabberId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}