package com.example.madcamp_pj1.ui.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.madcamp_pj1.R;

import java.io.File;

public class BigFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_big, container, false);
        ImageView image = rootView.findViewById(R.id.big_image);
        int position = getArguments().getInt("position");
        File filesDir = getActivity().getFilesDir();
        File file = new File(filesDir, "img" + position + ".png");
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        image.setImageBitmap(bitmap);

        Button deleteButton = rootView.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(v -> {
            file.delete();
            int count = position + 1;
            while (true){
                File oldFile = new File(filesDir, "img" + count + ".png");
                if(oldFile.exists()){
                    File newName = new File(filesDir, "img" + (count - 1) + ".png");
                    oldFile.renameTo(newName);
                    count++;
                }
                else break;
            }
            backAndRefreshParentFragment();
        });

        Button confirmButton = rootView.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> backToParentFragment());

        return rootView;
    }
    private void backToParentFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(BigFragment.this).commit();
    }
    private void backAndRefreshParentFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(BigFragment.this);
        fragmentManager.popBackStack();
        GalleryFragment galleryFragment = new GalleryFragment();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(R.id.nav_host_fragment, galleryFragment);
        transaction.commit();
    }
}
