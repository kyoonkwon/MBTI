package com.example.madcamp_pj1.ui.method;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;

import androidx.annotation.RequiresApi;

import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;

public class Device {
    @RequiresApi(api = JELLY_BEAN_MR1)
    public static int getScreenWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        int width = size.x;
        return width;
    }

    @RequiresApi(api = JELLY_BEAN_MR1)
    public static int getGalleryColumnWidth(Activity activity ) {
        int width = getScreenWidth(activity);
        Resources res;
        res = activity.getResources();
        int horizontalSpacing = (int) (res.getDimension(com.example.madcamp_pj1.R.dimen.gallery_grid_spacing) / res.getDisplayMetrics().density);
        int size = (width - 4 * horizontalSpacing) / 3;
        return size;
    }
    @RequiresApi(api = JELLY_BEAN_MR1)
    public static Bitmap createThumbnail(Bitmap bitmap, Activity activity) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int imgSize = getGalleryColumnWidth(activity);
        if (height > width)
            bitmap = Bitmap.createBitmap(bitmap, 0, (height - width) / 2, width, width);
        else
            bitmap = Bitmap.createBitmap(bitmap, (width - height) / 2, 0, height, height);
        bitmap = Bitmap.createScaledBitmap(bitmap, imgSize, imgSize, true);

        return bitmap;
    }

}
