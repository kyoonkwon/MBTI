package com.example.madcamp_pj1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private ArrayList<FriendItem> mFriendList;

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
        void onEditClick(View v, int position);
        void onCallClick(View v, int position);
    }
    static private OnItemClickListener mListener = null;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    @NonNull
    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerAdapter.ViewHolder holder, int position) {
        holder.onBind(mFriendList.get(position));
    }

    public void setFriendList(ArrayList<FriendItem> list){
        this.mFriendList = list;
        notifyDataSetChanged();
    }

    public FriendItem getItem(int position){
        return mFriendList.get(position);
    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name;
        TextView message;
        ImageButton call;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.profile);
            name = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message);
            call = itemView.findViewById(R.id.callBtn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onEditClick(view, position);
                        }
                    }
                }
            });

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onCallClick(view, position);
                        }
                    }
                }
            });

        }

        void onBind(FriendItem item){
            profile.setImageBitmap(item.getBitmap());
            name.setText(item.getName());
            message.setText(item.getMessage());
        }
    }
}