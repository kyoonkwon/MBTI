package com.example.madcamp_pj1.ui.gallery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
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
import com.example.madcamp_pj1.ui.method.Device;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class GalleryFragment extends Fragment {

    private static final int GALLERY_REQUEST = 101;
    private static final int CAMERA_REQUEST = 111;
    private static final int CAMERA_BUTTON_POSITION = 0;

    private GridView m_grid;
    private GalleryAdapter m_gallAdt;

    private void getGalleryPermission(Activity activity, Context context) {
        String temp = "";
        int permissionForGalleryRead = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionForGalleryRead != PackageManager.PERMISSION_GRANTED)
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";

        int permissionForGalleryWrite = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionForGalleryWrite != PackageManager.PERMISSION_GRANTED)
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        if (!temp.isEmpty())
            ActivityCompat.requestPermissions(activity, temp.trim().split(" "), 1);
    }

    private void getCameraPermission(Activity activity, Context context) {
        String temp = "";
        int permissionForCamera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if (permissionForCamera != PackageManager.PERMISSION_GRANTED)
            temp = Manifest.permission.CAMERA;
        if (!temp.isEmpty())
            ActivityCompat.requestPermissions(activity, temp.trim().split(" "), 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Bitmap bm = null;
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            try {
                InputStream is = getContext().getContentResolver().openInputStream(data.getData());
                bm = BitmapFactory.decodeStream(is);
                is.close();

                File filesDir = getActivity().getFilesDir();
                File file = new File(filesDir, "img" + m_gallAdt.getCount() + ".png");
                FileOutputStream out = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            try {
                Bundle extras = data.getExtras();
                bm = (Bitmap) extras.get("data");

                File filesDir = getActivity().getFilesDir();
                File file = new File(filesDir, "img" + m_gallAdt.getCount() + ".png");
                FileOutputStream out;
                out = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            File filesDir = getActivity().getFilesDir();
            File thumbnailOut = new File(filesDir, "thumbnail" + m_gallAdt.getCount() + ".png");
            FileOutputStream out = null;

            out = new FileOutputStream(thumbnailOut);

            Bitmap thumbnail = Device.createThumbnail(bm, getActivity());
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            m_gallAdt.setItem(thumbnail);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeItemInAdapter(int position) {
        m_gallAdt.deleteItem(position);
        refresh();
    }

    public void refresh() {
        m_gallAdt.notifyDataSetChanged();
        m_grid.setAdapter(m_gallAdt);
    }

    private void thumbnailOnClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);

        MemoFragment memoFragment = new MemoFragment();
        memoFragment.setArguments(bundle);

        getParentFragmentManager()
                .beginTransaction()
                .add(R.id.nav_host_fragment, memoFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private void cameraOnClick() {
        Activity activity = getActivity();
        Context context = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("새로 스크랩 하기");

        builder.setNegativeButton("카메라로 찍기", (dialog, which) -> {
            getCameraPermission(activity, context);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST);
        });

        builder.setPositiveButton("갤러리에서 가져오기", (dialog, which) -> {
            getGalleryPermission(activity, context);
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, GALLERY_REQUEST);
        });

        builder.setNeutralButton("취소", (dialog, which) -> Toast.makeText(context, "취소", Toast.LENGTH_SHORT).show());

        AlertDialog dialog = builder.create();

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),"fonts/pnm.otf");

        dialog.show();

        TextView title = dialog.findViewById(android.R.id.message);
        title.setTextSize(20);
        title.setTypeface(face);

        TextView button = dialog.findViewById(android.R.id.button1);
        button.setTypeface(face);
        button.setTextColor(0xFF000000);

        button = dialog.findViewById(android.R.id.button2);
        button.setTypeface(face);
        button.setTextColor(0xFF000000);

        button = dialog.findViewById(android.R.id.button3);
        button.setTypeface(face);
        button.setTextColor(0xFF000000);


    }

    @SuppressLint({"ClickableViewAccessibility", "WrongThread"})
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();
        Context context = container.getContext();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_gallery, container, false);

        m_grid = rootView.findViewById(R.id.grid_gallery);
        OnPinchListener onPinchListener = new OnPinchListener();
        ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(context, onPinchListener);
        m_grid.setOnTouchListener((v, event) -> {
            scaleGestureDetector.onTouchEvent(event);
            return false;
        });

        m_grid.setOnItemClickListener((parent, view, position, id) -> {
            if (position == CAMERA_BUTTON_POSITION)
                cameraOnClick();
            else
                thumbnailOnClick(position);
        });

        m_gallAdt = new GalleryAdapter(context, activity);

        try {
            AssetManager am = context.getAssets();
            BufferedInputStream buf;
            Bitmap bitmap;
            buf = new BufferedInputStream(am.open("camera.png"));
            bitmap = BitmapFactory.decodeStream(buf);
            m_gallAdt.setItem(bitmap);
            buf.close();

            int count = 1;
            while (true) {
                File filesDir = getActivity().getFilesDir();
                File file = new File(filesDir, "thumbnail" + count + ".png");
                if (file.exists()) {
                    bitmap = BitmapFactory.decodeFile(file.getPath());
                    m_gallAdt.setItem(bitmap);
                    count++;
                } else break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        m_grid.setAdapter(m_gallAdt);

        return rootView;
    }

    private class OnPinchListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            int colNumDif = -1 * (int) ((scaleFactor - 1) * 12);
            int newColNum = m_grid.getNumColumns() + colNumDif;
            if (newColNum <= 1) m_grid.setNumColumns(1);
            else if (newColNum >= m_gallAdt.getCount()) m_grid.setNumColumns(m_gallAdt.getCount());
            else m_grid.setNumColumns(newColNum);
            return true;
        }
    }
}
