package com.example.madcamp_pj1.ui.home;

import android.Manifest;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcamp_pj1.FriendItem;
import com.example.madcamp_pj1.MyRecyclerAdapter;
import com.example.madcamp_pj1.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter mRecyclerAdapter;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView);

        /* initiate adapter */
        mRecyclerAdapter = new MyRecyclerAdapter();

        /* initiate recyclerview */
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Log.e("homeFrag", "data");

        getContactList();

        return view;
    }

    void getContactListAsLog() {
        ArrayList<ContactInfo> list = getContactList();

        for (ContactInfo info : list) {
            Log.i("contact", info.toString());
        }
    }

    public ArrayList<ContactInfo> getContactList() {
        ArrayList<ContactInfo> list = new ArrayList<>();
        ArrayList<FriendItem> mfriendItems = new ArrayList<>();

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
        };

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, sortOrder);
        if (cursor.moveToFirst()) {
            do {
                ContactInfo info = new ContactInfo();

                info.id = cursor.getLong(0);
                info.photoId = cursor.getLong(1);
                info.displayName = cursor.getString(2);
                info.phoneNumber = cursor.getString(3);
                list.add(info);
                mfriendItems.add(new FriendItem(R.drawable.ic_home_black_24dp,info.displayName,info.phoneNumber));


            } while (cursor.moveToNext());
        }

        mRecyclerAdapter.setFriendList(mfriendItems);

        return list;
    }

//    private Bitmap getContactPicture(long contactId) {
//
//        Bitmap bm = null;
//
//        try {
//            InputStream inputStream = ContactsContract.Contacts
//                    .openContactPhotoInputStream(getContentResolver(),
//                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId));
//
//            if (inputStream != null) {
//                bm = BitmapFactory.decodeStream(inputStream);
//                inputStream.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return bm;
//    }

    class ContactInfo {

        long id;
        String displayName;
        String phoneNumber;
        long photoId;

        @Override
        public String toString() {
            return "ContactInfo{" +
                    "id=" + id +
                    ", displayName='" + displayName + '\'' +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    ", photoId=" + photoId +
                    '}';
        }
    }
}