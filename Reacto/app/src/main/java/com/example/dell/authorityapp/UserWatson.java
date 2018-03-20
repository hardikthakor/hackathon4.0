package com.example.dell.authorityapp;

/**
 * Created by Dell on 14/02/2018.
 */
import android.graphics.Bitmap;

import com.github.bassaer.chatmessageview.model.IChatUser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
public class UserWatson implements IChatUser{

    Integer id;
    String name;
    Bitmap icon;

    public UserWatson(int id, String name, Bitmap icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    @NotNull
    @Override
    public String getId() {
        return id.toString();
    }

    @Nullable
    @Override
    public String getName() {
        return name;
    }

    @Nullable
    @Override
    public Bitmap getIcon() {
        return icon;
    }

    @Override
    public void setIcon(Bitmap bitmap) {
        this.icon = icon;
    }
}
