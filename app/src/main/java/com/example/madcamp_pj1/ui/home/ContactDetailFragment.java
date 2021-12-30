package com.example.madcamp_pj1.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.madcamp_pj1.FriendItem;
import com.example.madcamp_pj1.R;
import com.example.madcamp_pj1.ui.gallery.BigFragment;
import com.example.madcamp_pj1.ui.gallery.GalleryFragment;

import java.util.List;

public class ContactDetailFragment extends Fragment {

    private Button closeBtn;
    private Button fixBtn;
    private Button delBtn;
    private ImageView img;
    private TextView name;
    private TextView phone;


    public static ContactDetailFragment newInstance() {
        return new ContactDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contactdetail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FriendItem fi = getArguments().getParcelable("fi");


        closeBtn = view.findViewById(R.id.detailCloseBtn);
        closeBtn.setOnClickListener(v -> backToParentFragment());

        name = view.findViewById(R.id.detailName);
        name.setText(fi.getName());
        phone = view.findViewById(R.id.detailPhone);
        phone.setText(fi.getMessage());
        img = view.findViewById(R.id.detailImg);
        img.setImageBitmap(fi.getBitmap());

        fixBtn = view.findViewById(R.id.detailFixBtn);
        fixBtn.setOnClickListener(v -> fixContact(fi));

        delBtn = view.findViewById(R.id.detailDelBtn);
        delBtn.setOnClickListener(v -> delContact(fi));


    }

    private void backToParentFragment() {
        getParentFragmentManager()
                .beginTransaction()
                .remove(ContactDetailFragment.this)
                .commit();
    }

    private void backAndRefreshParentFragment() {
        List<Fragment> fragmentList =  getParentFragmentManager().getFragments();
        for(Fragment fragment : fragmentList)
            if(fragment.getClass() == HomeFragment.class){
                ((HomeFragment) fragment).refreshShowDetail(getArguments().getInt("pos"));
                break;
            }
        Log.i("contact1", "back");
        backToParentFragment();

    }

    void delContact(FriendItem fi) {
        List<Fragment> fragmentList =  getParentFragmentManager().getFragments();
        for(Fragment fragment : fragmentList)
            if(fragment.getClass() == HomeFragment.class){
                ((HomeFragment) fragment).delContact(fi.getKey());
                break;
            }


        backToParentFragment();
    }

    void fixContact(FriendItem fi){

        Uri selectedUri = ContactsContract.Contacts.getLookupUri(fi.getId(), fi.getKey());
        Intent editIntent = new Intent(Intent.ACTION_EDIT);
        editIntent.setDataAndType(selectedUri, ContactsContract.Contacts.CONTENT_ITEM_TYPE);
        editIntent.putExtra("finishActivityOnSaveCompleted", true);
        startActivityForResult(editIntent, 10);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            Log.i("contact1", "frag2");
            backAndRefreshParentFragment();
            //backToParentFragment();
        }
    }
}
