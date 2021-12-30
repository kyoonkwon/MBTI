package com.example.madcamp_pj1.ui.gallery;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.madcamp_pj1.MainActivity;
import com.example.madcamp_pj1.R;
import com.example.madcamp_pj1.ui.Device;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class GalleryFragment extends Fragment {

    final int GALLERY_REQUEST = 101;
    final int CAMERA_REQUEST = 111;
    final int CAMERA_BUTTON_POSITION = 0;

    private GridView m_grid;
    private GalleryAdapter m_gallAdt;

    private void getGalleryPermission(Activity activity, Context context){
        String temp = "";
        int permissionForGalleryRead = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permissionForGalleryRead != PackageManager.PERMISSION_GRANTED)
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";

        int permissionForGalleryWrite = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionForGalleryWrite != PackageManager.PERMISSION_GRANTED)
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        if (!temp.isEmpty())
            ActivityCompat.requestPermissions(activity, temp.trim().split(" "), 1);
    }

    private void getCameraPermission(Activity activity, Context context){
        String temp = "";
        int permissionForCamera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if(permissionForCamera != PackageManager.PERMISSION_GRANTED)
            temp = Manifest.permission.CAMERA;
        if(!temp.isEmpty())
            ActivityCompat.requestPermissions(activity, temp.trim().split(" "), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            try{
                InputStream is = getContext().getContentResolver().openInputStream(data.getData());
                Bitmap bm = BitmapFactory.decodeStream(is);
                is.close();

                File filesDir = getActivity().getFilesDir();
                File file = new File(filesDir, "img" + m_gallAdt.getCount() + ".png");
                FileOutputStream out = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();

                m_gallAdt.setItem(bm);
                refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
            try {
                Bundle extras = data.getExtras();
                Bitmap bm = (Bitmap)extras.get("data");

                File filesDir = getActivity().getFilesDir();
                File file = new File(filesDir, "img" + m_gallAdt.getCount() + ".png");
                FileOutputStream out;
                out = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();

                m_gallAdt.setItem(bm);
                refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void refresh(){
        m_gallAdt.notifyDataSetChanged();
        m_grid.setAdapter(m_gallAdt);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainActivity activity = (MainActivity)getActivity();
        Context context = container.getContext();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_gallery, container, false);

        m_grid = (GridView) rootView.findViewById(R.id.grid_gallery);

        m_grid.setOnItemClickListener((parent, view, position, id) -> {
            if(position == CAMERA_BUTTON_POSITION) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("이미지 가져오기");
                builder.setMessage("어디서 가져올지 정하세용");

                builder.setNegativeButton("카메라", (dialog, which) -> {
                    getCameraPermission(activity, context);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_REQUEST);
                });

                builder.setPositiveButton("갤러리", (dialog, which) -> {
                    getGalleryPermission(activity, context);
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, GALLERY_REQUEST);
                });

                builder.setNeutralButton("취소", (dialog, which) -> Toast.makeText(context, "Cancel Click", Toast.LENGTH_SHORT).show());

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                BigFragment bigFragment = new BigFragment();
                bigFragment.setArguments(bundle);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.add(R.id.nav_host_fragment, bigFragment);
                transaction.commit();
            }
        });

        int size = Device.getGalleryColumnWidth(activity);
        m_gallAdt = new GalleryAdapter(context, size);

        try{
            AssetManager am = context.getAssets();
            BufferedInputStream buf;
            Bitmap bitmap;
            buf = new BufferedInputStream(am.open("camera.png"));
            bitmap = BitmapFactory.decodeStream(buf);
            m_gallAdt.setItem(bitmap);
            buf.close();

            int count = 1;
            while (true){
                File filesDir = getActivity().getFilesDir();
                File file = new File(filesDir, "img" + count + ".png");
                if(file.exists()){
                    bitmap = BitmapFactory.decodeFile(file.getPath());
                    m_gallAdt.setItem(bitmap);
                    count++;
                }
                else break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        m_grid.setAdapter(m_gallAdt);

        return rootView;
    }
}