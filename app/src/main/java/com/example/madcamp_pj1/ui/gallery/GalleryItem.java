package com.example.madcamp_pj1.ui.gallery;

import android.graphics.Bitmap;

public class GalleryItem {
    private final Bitmap bitmap;

    public GalleryItem(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getItemBitmap() {
        return this.bitmap;
    }
}
