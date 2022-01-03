package com.example.madcamp_pj1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.madcamp_pj1.ui.gallery.MemoFragment;
import com.example.madcamp_pj1.ui.method.Device;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragmentList) {
            List<Fragment> childFragments = fragment.getChildFragmentManager().getFragments();
            for (Fragment childFragment : childFragments) {
                if (childFragment instanceof MemoFragment) {
                    ((MemoFragment) childFragment).onBackPressed();
                    return;
                }
            }
        }
        super.onBackPressed();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_image, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        File filesDir = this.getFilesDir();
        File isInitialized = new File(filesDir, "isInitialized.tmp");
        //isInitialized.delete();
        try {
            if (isInitialized.createNewFile()) {
                AssetManager am = this.getAssets();
                for (int i = 1; i < 21; ++i) {
                    try {
                        File file = new File(filesDir, "img" + i + ".png");
                        BufferedInputStream buf = new BufferedInputStream(am.open("img" + i + ".png"));
                        Bitmap bitmap = BitmapFactory.decodeStream(buf);
                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();

                        File thumbnailOut = new File(filesDir, "thumbnail" + i + ".png");
                        out = new FileOutputStream(thumbnailOut);
                        Bitmap thumbnail = Device.createThumbnail(bitmap, this);
                        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[]{Manifest.permission.READ_CONTACTS};
            ActivityCompat.requestPermissions(this, permissions, 100);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[]{Manifest.permission.WRITE_CONTACTS};
            ActivityCompat.requestPermissions(this, permissions, 100);
        }


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();

            if (view != null && view instanceof EditText) {
                Rect r = new Rect();
                view.getGlobalVisibleRect(r);
                int rawX = (int) ev.getRawX();
                int rawY = (int) ev.getRawY();
                if (!r.contains(rawX, rawY)) {
                    hideSoftKeyboard(MainActivity.this);
                }
            }
        }


        return super.dispatchTouchEvent(ev);
    }
}