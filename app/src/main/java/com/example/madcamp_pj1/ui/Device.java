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
    public static int getScreenHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        int height = size.y;
        return height;
    }

}
