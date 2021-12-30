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

public class GalleryAdapter extends BaseAdapter{
    private Context m_context;
    private ArrayList<GalleryItem> m_array;

    public int imgSize;
    public GalleryAdapter(Context context, int size) {
        this.imgSize = size;
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
        bitmap = createThumbnail(bitmap);
        imageView.setImageBitmap(bitmap);

        return convertView;
    }

    public Bitmap createThumbnail(Bitmap bitmap){
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        if(height > width)
            bitmap = Bitmap.createBitmap(bitmap, 0, (height - width)/2, width, width);
        else
            bitmap = Bitmap.createBitmap(bitmap, (width - height)/2, 0, height, height);
        bitmap = Bitmap.createScaledBitmap(bitmap, imgSize, imgSize,true);

        return bitmap;
    }

    public void setItem(Bitmap bitmap) {
        this.m_array.add(new GalleryItem(bitmap));
    }

    public Bitmap getItemBitmap(int position) {
        return this.m_array.get(position).getItemBitmap();
    }
}