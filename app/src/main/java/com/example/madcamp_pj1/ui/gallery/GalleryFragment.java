package com.example.madcamp_pj1.ui.gallery;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.madcamp_pj1.MainActivity;
import com.example.madcamp_pj1.R;
import com.example.madcamp_pj1.ui.Device;

import java.io.BufferedInputStream;

public class GalleryFragment extends Fragment {

    private GridView m_grid;
    private GalleryAdapter m_gallAdt;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainActivity activity = (MainActivity)getActivity();
        Context context = container.getContext();

        String temp = "";
        int permissionForGalleryRead = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permissionForGalleryRead != PackageManager.PERMISSION_GRANTED)
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";

        int permissionForGalleryWrite = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionForGalleryWrite != PackageManager.PERMISSION_GRANTED)
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        if (!temp.isEmpty())
            ActivityCompat.requestPermissions(activity, temp.trim().split(" "), 1);

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 101);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_gallery, container, false);


        m_grid = (GridView) rootView.findViewById(R.id.grid_gallery);
        int width = Device.getScreenWidth(activity);
        int size = (width - 12) / 3;
        m_gallAdt = new GalleryAdapter(context, size);

        try{
            AssetManager am = context.getAssets();
            BufferedInputStream buf;
            buf = new BufferedInputStream(am.open("camera.png"));
            Bitmap bitmap = BitmapFactory.decodeStream(buf);
            m_gallAdt.setItem(bitmap, true);
            for(int i=1; i<21; ++i) {
                buf = new BufferedInputStream(am.open("img"+i+".png"));
                bitmap = BitmapFactory.decodeStream(buf);
                m_gallAdt.setItem(bitmap, false);
            }

            buf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        m_grid.setAdapter(m_gallAdt);

        return rootView;
    }
}