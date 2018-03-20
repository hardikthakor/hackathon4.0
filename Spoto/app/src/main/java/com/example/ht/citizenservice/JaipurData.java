package com.example.ht.citizenservice;

import android.graphics.Bitmap;

/**
 * Created by CGT on 18-03-2018.
 */

public class JaipurData {

    private String desDate;
    private String srcDate;
    private String url;
    private String name;
    private String des;
    private Bitmap bitmap;

    public JaipurData(String srcDate, String desDate, String name, String des, String url, Bitmap bitmap){
        this.srcDate = srcDate;
        this.desDate = desDate;
        this.name = name;
        this.des = des;
        this.url = url;
        this.bitmap = bitmap;
    }

    public String getDesDate() {
        return desDate;
    }

    public void setDesDate(String desDate) {
        this.desDate = desDate;
    }

    public String getSrcDate() {
        return srcDate;
    }

    public void setSrcDate(String srcDate) {
        this.srcDate = srcDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
