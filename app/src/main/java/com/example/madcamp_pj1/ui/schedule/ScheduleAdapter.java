package com.example.madcamp_pj1.ui.schedule;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcamp_pj1.R;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private ArrayList<Schedule> mScheduleList;

    public ScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(mScheduleList.get(position));
    }

    @Override
    public int getItemCount() {
        return mScheduleList.size();
    }

    public Schedule getItem(int position) {return mScheduleList.get(position); }
    public void setScheduleList(ArrayList<Schedule> list) {
        this.mScheduleList = list;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView time;
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch swtich;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.itemScheduleName);
            time = itemView.findViewById(R.id.itemScheduleTime);
            swtich = itemView.findViewById(R.id.itemScheduleSwitch);

        }

        void onBind(Schedule item){
            name.setText(item.getName());
            String fromto = item.getStartTimeAsString() + " - " + item.getEndTimeAsString();
            time.setText(fromto);
            swtich.setChecked(item.getAlarm());
        }
    }
}
