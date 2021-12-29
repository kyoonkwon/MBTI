package com.example.madcamp_pj1.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Toast;


import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
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
    private Button addContactBtn;
    private EditText contactName;
    private EditText contactPhone;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Log.i("contact1", "create");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mRecyclerAdapter = new MyRecyclerAdapter();
        contactList = getContactList();
        mRecyclerView = view.findViewById(R.id.recyclerView);

        /* initiate recyclerview */
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        addContactBtn = view.findViewById(R.id.addContact);
        contactName = view.findViewById(R.id.contactName);
        contactPhone = view.findViewById(R.id.contactPhone);

        mRecyclerAdapter.setOnItemClickListener(new MyRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(View v, int position) {
                FriendItem fi = mRecyclerAdapter.getItem(position);
                fixContact(fi);
            }

            @Override
            public void onCallClick(View v, int position) {
                FriendItem fi = mRecyclerAdapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+fi.getMessage()));
                startActivity(intent);
            }
        });

        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                intent.putExtra(ContactsContract.Intents.Insert.NAME, contactName.getText().toString());
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, contactPhone.getText());
                intent.putExtra("finishActivityOnSaveCompleted", true);

                contactName.setText("");
                contactPhone.setText("");

                startActivityForResult(intent, 10);
            }
        });


        getContactListAsLog();
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

    void getContactListAsLog() {

        ArrayList<FriendItem> mfriendItems = new ArrayList<>();

        for (ContactInfo info : contactList) {
            Log.i("contact1", info.toString());
            mfriendItems.add(new FriendItem(R.mipmap.ic_phonecall,info.displayName,info.phoneNumber, info.id, info.key));
        }
        mRecyclerAdapter.setFriendList(mfriendItems);
    }

    public ArrayList<ContactInfo> getContactList() {
        ArrayList<ContactInfo> list = new ArrayList<>();

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.Contacts.LOOKUP_KEY,
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
                info.key = cursor.getString(4);
                list.add(info);


            } while (cursor.moveToNext());
        }


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