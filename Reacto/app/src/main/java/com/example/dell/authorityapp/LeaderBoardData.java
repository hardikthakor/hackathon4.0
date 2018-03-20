package com.example.dell.authorityapp;

/**
 * Created by Dell on 14/02/2018.
 */
import android.graphics.Bitmap;
public class LeaderBoardData {

    private String id;
    private String name;
    private Bitmap bitmap;
    private String points;
    private int medalimage;

    public LeaderBoardData(String test, String onee, Bitmap bitmap, String points, int medalimage){
        this.id = test;
        this.name = onee;
        this.bitmap = bitmap;
        this.points = points;
        this.medalimage = medalimage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public int getMedalimage() {
        return medalimage;
    }

    public void setMedalimage(int medalimage) {
        this.medalimage = medalimage;
    }
}
