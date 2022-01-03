package com.example.madcamp_pj1.ui.gallery;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.madcamp_pj1.R;

import java.util.ArrayList;

public class GalleryAdapter extends BaseAdapter {
    private final Context m_context;
    private final ArrayList<GalleryItem> m_array;


    public GalleryAdapter(Context context, Activity activity) {
        this.m_context = context;
        this.m_array = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return this.m_array.size();
    }

    @Override
    public GalleryItem getItem(int position) {
        return this.m_array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.gallery_item, parent, false);
        ImageView imageView = convertView.findViewById(R.id.item_image);

        Bitmap bitmap = getItemBitmap(position);
        imageView.setImageBitmap(bitmap);

        return convertView;
    }

    public void setItem(Bitmap bitmap) {
        this.m_array.add(new GalleryItem(bitmap));
    }

    public void deleteItem(int position) {
        this.m_array.remove(position);
    }

    public Bitmap getItemBitmap(int position) {
        return this.m_array.get(position).getItemBitmap();
    }
}