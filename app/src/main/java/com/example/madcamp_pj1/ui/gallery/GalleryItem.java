package com.example.madcamp_pj1.ui.gallery;

import android.graphics.Bitmap;

public class GalleryItem {
    private Bitmap m_img;

    public boolean isCamera;

    public GalleryItem(Bitmap m_img, boolean isCamera) {
        this.m_img = m_img;
        this.isCamera = isCamera;
    }

    public Bitmap getItemImg() {
        return this.m_img;
    }
}
