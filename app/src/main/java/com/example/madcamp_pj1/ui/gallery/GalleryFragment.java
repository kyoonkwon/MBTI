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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.madcamp_pj1.MainActivity;
import com.example.madcamp_pj1.R;
import com.example.madcamp_pj1.ui.Device;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class GalleryFragment extends Fragment {

    private GridView m_grid;
    private GalleryAdapter m_gallAdt;
    private int size;

    public void getGalleryPermission(Activity activity, Context context){
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

    public void getCameraPermission(Activity activity, Context context){
        String req = "";
        int permissionForCamera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if(permissionForCamera != PackageManager.PERMISSION_GRANTED)
            req = Manifest.permission.CAMERA;
        if(!req.isEmpty())
            ActivityCompat.requestPermissions(activity, req.trim().split(" "), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 101 && resultCode == RESULT_OK){
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
        } else if(requestCode == 111 && resultCode == RESULT_OK){
            try {
                Bundle extras = data.getExtras();
                Bitmap bm = (Bitmap)extras.get("data");
                File filesDir = getActivity().getFilesDir();
                File file = new File(filesDir, "img" + m_gallAdt.getCount() + ".png");
                FileOutputStream out = null;

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

    public void refresh(){
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

        m_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(activity);
                    dlg.setTitle("이미지 가져오기");
                    dlg.setMessage("어디서 가져올지 정하세용");
                    dlg.setNegativeButton("카메라", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getCameraPermission(activity, context);
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 111);
                        }
                    });
                    dlg.setPositiveButton("갤러리", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getGalleryPermission(activity, context);
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, 101);
                        }
                    });
                    dlg.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "Cancel Click", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog dialog = dlg.create();
                    dialog.show();

                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                    BigFragment bigFragment = new BigFragment();
                    bigFragment.setArguments(bundle);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    // transaction.replace(R.id.nav_host_fragment, bigFragment);
                    transaction.add(R.id.nav_host_fragment, bigFragment);
                    transaction.commit();
                }
            }
        });

        int width = Device.getScreenWidth(activity);
        size = (width - 12) / 3;
        m_gallAdt = new GalleryAdapter(context, size, activity);

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