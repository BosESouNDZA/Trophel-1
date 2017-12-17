package com.example.bhurivatmontri.trophel.adapter;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by Bhurivat Montri on 10/20/2017.
 */

public class EndangeredItem {
    private String mName;
    private int mThumbnail;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.mThumbnail = thumbnail;
    }

}
