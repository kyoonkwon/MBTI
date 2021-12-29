package com.example.madcamp_pj1.ui.gallery;

import android.graphics.Bitmap;

public class GalleryItem {
    private Bitmap m_img;

    public GalleryItem(Bitmap m_img) {
        this.m_img = m_img;
    }

    public Bitmap getItemImg() {
        return this.m_img;
    }
}
