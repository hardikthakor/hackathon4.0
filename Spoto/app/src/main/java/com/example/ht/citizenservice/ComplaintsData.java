package com.example.ht.citizenservice;

import android.graphics.Bitmap;

/**
 * Created by CGT on 12-02-2018.
 */

public class ComplaintsData {

    private String timelineStatus;
    private String complaintsTitle;
    private String AJabberId;
    private Bitmap bitmap;

    public ComplaintsData(String complaintsTitle, String timelineStatus,String AJabberId, Bitmap bitmap){
        this.complaintsTitle = complaintsTitle;
        this.timelineStatus = timelineStatus;
        this.AJabberId = AJabberId;
        this.bitmap = bitmap;
    }

    public String getAJabberId() {
        return AJabberId;
    }

    public void setAJabberId(String AJabberId) {
        this.AJabberId = AJabberId;
    }

    public String getTimelineStatus() {
        return timelineStatus;
    }

    public void setTimelineStatus(String timelineStatus) {
        this.timelineStatus = timelineStatus;
    }

    public String getComplaintsTitle() {
        return complaintsTitle;
    }

    public void setComplaintsTitle(String complaintsTitle) {
        this.complaintsTitle = complaintsTitle;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
