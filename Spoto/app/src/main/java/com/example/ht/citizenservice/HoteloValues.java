package com.example.ht.citizenservice;

/**
 * Created by CGT on 19-03-2018.
 */

public class HoteloValues {

    String hlong;
    String hlat;
    String hname;
    String desc;
    String hurl;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getHurl() {
        return hurl;
    }

    public void setHurl(String hurl) {
        this.hurl = hurl;
    }

    public String getHlong() {
        return hlong;
    }

    public void setHlong(String hlong) {
        this.hlong = hlong;
    }

    public String getHlat() {
        return hlat;
    }

    public void setHlat(String hlat) {
        this.hlat = hlat;
    }

    public String getHname() {
        return hname;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }

    public String getHrating() {
        return hrating;
    }

    public void setHrating(String hrating) {
        this.hrating = hrating;
    }

    String hrating;

    public HoteloValues(String hlat,String hlong,String hname,String hrating,String desc,String hurl)
    {
        //this.bitmap=bitmap;
        this.hlat = hlat;
        this.hlong = hlong;
        this.hname = hname;
        this.hrating = hrating;
        this.desc=desc;
        this.hurl=hurl;



    }

}
