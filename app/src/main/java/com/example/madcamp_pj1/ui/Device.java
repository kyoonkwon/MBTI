package com.example.madcamp_pj1.ui;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;

import androidx.annotation.RequiresApi;

import static android.os.Build.VERSION_CODES.*;

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
    public static int getGalleryColumnWidth(Activity activity) {
        int width = getScreenWidth(activity);
        int numOfColumns = com.example.madcamp_pj1.R.dimen.gallery_num_of_columns;
        int horizontalSpacing = com.example.madcamp_pj1.R.dimen.gallery_horizontal_spacing;
        int size = (width - (numOfColumns+1) * horizontalSpacing) / numOfColumns;
        return size;
    }

}
