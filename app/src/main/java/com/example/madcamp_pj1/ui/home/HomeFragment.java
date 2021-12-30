package com.example.madcamp_pj1.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcamp_pj1.FriendItem;
import com.example.madcamp_pj1.MyRecyclerAdapter;
import com.example.madcamp_pj1.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter mRecyclerAdapter;
    private ArrayList<ContactInfo> contactList;
    private ImageButton addContactBtn;
    private SearchView searchView;
    private String prevText;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("contact1", "created");

        mRecyclerAdapter = new MyRecyclerAdapter();
        if(contactList == null) {
            contactList = getContactList("");
            prevText = "";
        }
        mRecyclerView = view.findViewById(R.id.recyclerView);

        /* initiate recyclerview */
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        addContactBtn = view.findViewById(R.id.addContact);

        mRecyclerAdapter.setOnItemClickListener(new MyRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {

            }

            @Override
            public void onEditClick(View v, int position) {
                FriendItem fi = mRecyclerAdapter.getItem(position);
                fixContact(fi);

            }

            @Override
            public void onCallClick(View v, int position) {
                FriendItem fi = mRecyclerAdapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + fi.getMessage()));
                startActivity(intent);
            }
        });

        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

//                intent.putExtra(ContactsContract.Intents.Insert.NAME, contactName.getText().toString());
//                intent.putExtra(ContactsContract.Intents.Insert.PHONE, contactPhone.getText());
                intent.putExtra("finishActivityOnSaveCompleted", true);

//                contactName.setText("");
//                contactPhone.setText("");

                startActivityForResult(intent, 10);
            }
        });


        getContactListAsLog();

        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("contact1 submit", query);


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("contact1 change", newText);
                contactList = getContactList(newText);
                getContactListAsLog();
                mRecyclerAdapter.notifyDataSetChanged();
                return true;
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            FragmentTransaction ft = this.getParentFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }

    void fixContact(FriendItem fi){

        Uri selectedUri = ContactsContract.Contacts.getLookupUri(fi.getId(), fi.getKey());
        Intent editIntent = new Intent(Intent.ACTION_EDIT);
        editIntent.setDataAndType(selectedUri, ContactsContract.Contacts.CONTENT_ITEM_TYPE);
        editIntent.putExtra("finishActivityOnSaveCompleted", true);
        startActivityForResult(editIntent, 10);

    }

    private Bitmap queryContactImage(int imageDataRow) {
        Cursor c = getActivity().getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[] {
                ContactsContract.CommonDataKinds.Photo.PHOTO
        }, ContactsContract.Data._ID + "=?", new String[] {
                Integer.toString(imageDataRow)
        }, null);
        byte[] imageBytes = null;
        if (c != null) {
            if (c.moveToFirst()) {
                imageBytes = c.getBlob(0);
            }
            c.close();
        }

        if (imageBytes != null) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            return null;
        }
    }

    private Bitmap getRoundedCroppedBitmap(Bitmap bitmap) {
        int widthLight = bitmap.getWidth();
        int heightLight = bitmap.getHeight();

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        Paint paintColor = new Paint();
        paintColor.setFlags(Paint.ANTI_ALIAS_FLAG);

        RectF rectF = new RectF(new Rect(0, 0, widthLight, heightLight));

        canvas.drawRoundRect(rectF, widthLight / 2, heightLight / 2, paintColor);

        Paint paintImage = new Paint();
        paintImage.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(bitmap, 0, 0, paintImage);

        return output;
    }

    void getContactListAsLog() {

        ArrayList<FriendItem> mfriendItems = new ArrayList<>();

        for (ContactInfo info : contactList) {
            Bitmap bitmap = queryContactImage((int) info.photoId);

            if(bitmap == null){
                bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_person);
            }
            bitmap = getRoundedCroppedBitmap(bitmap);
            mfriendItems.add(new FriendItem(bitmap,info.displayName,info.phoneNumber, info.id, info.key));
        }
        mRecyclerAdapter.setFriendList(mfriendItems);
    }

    public ArrayList<ContactInfo> getContactList(String name) {
        ArrayList<ContactInfo> list = new ArrayList<>();

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.Contacts.LOOKUP_KEY,
        };

        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE '%" + name +"%'";
        Log.i("contact1", selection);
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        Cursor cursor = getActivity().getContentResolver().query(uri, projection, selection, null, sortOrder);
        if (cursor.moveToFirst()) {
            do {
                ContactInfo info = new ContactInfo();

                info.id = cursor.getLong(0);
                info.photoId = cursor.getLong(1);
                info.displayName = cursor.getString(2);
                info.phoneNumber = cursor.getString(3);
                info.key = cursor.getString(4);
                list.add(info);


            } while (cursor.moveToNext());
        }


        return list;
    }


    class ContactInfo {

        long id;
        String displayName;
        String phoneNumber;
        long photoId;
        String key;

        @Override
        public String toString() {
            return "ContactInfo{" +
                    "id=" + String.valueOf(id) +
                    ", displayName='" + displayName + '\'' +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    ", photoId=" + photoId +
                    ", key=" + key +
                    '}';
        }
    }
}