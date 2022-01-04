package com.example.madcamp_pj1.ui.schedule;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcamp_pj1.R;
import com.example.madcamp_pj1.ui.home.MyRecyclerAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private final Activity m_activity;

    private ArrayList<Schedule> mScheduleList;

    static private OnItemClickListener mListener = null;


    public ScheduleAdapter(Activity activity) {
        m_activity = activity;
    }

    public interface OnItemClickListener {
        void onDeleteClick(View v, int position);
        void onSwitchClick(View v, int position, boolean isChekced);
        void onItemClick(View v, int position, View itemView);
    }

    public void setOnItemCLickListener(OnItemClickListener listener) {mListener = listener; }

    public ScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

    public Schedule getItem(int position) {
        return mScheduleList.get(position);
    }

    public void setScheduleList(ArrayList<Schedule> list) {
        this.mScheduleList = list;
        notifyDataSetChanged();
    }

    public void setScheduleItem(int position, Schedule schedule){
        this.mScheduleList.set(position, schedule);
        notifyDataSetChanged();
    }

    public static void setNotification(String when, String title, Activity m_activity) throws ParseException {

        Intent receiverIntent = new Intent(m_activity, AlarmReceiver.class);
        receiverIntent.putExtra("title", title);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(m_activity, title.hashCode(), receiverIntent, 0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);

        Log.e("TAG", title);

        when = getTime.concat(" " + when).concat(":00");
        Date datetime = dateFormat.parse(when);
        Log.e("NOTI", "SET:" + datetime.toString());
        Calendar calendar = Calendar.getInstance();
        long repeatInterval = AlarmManager.INTERVAL_DAY;
        long triggerTime = (SystemClock.elapsedRealtime()
                + repeatInterval);
        if (datetime.before(date)) {
            datetime.setTime(date.getTime() + repeatInterval);
        }

        calendar.setTime(datetime);
        AlarmManager alarmManager = (AlarmManager) m_activity.getSystemService(Context.ALARM_SERVICE);


        alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime, repeatInterval,
                pendingIntent
        );

        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView time;
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch swtich;
        ImageButton delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.itemScheduleName);
            time = itemView.findViewById(R.id.itemScheduleTime);
            swtich = itemView.findViewById(R.id.itemScheduleSwitch);
            delete = itemView.findViewById(R.id.scheduleRemove);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(view, getAdapterPosition(), itemView);
                }
            });


            swtich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mListener.onSwitchClick(buttonView, getAdapterPosition(), isChecked);
                    if (buttonView.isChecked()) {
                        Log.e("SWITCH", "ON");
                        String HHMM = (String) time.getText();
                        HHMM = HHMM.substring(0, 5);
                        try {
                            setNotification(HHMM, (String) name.getText(), m_activity);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("SWITCH", "OFF");
                        cancelNotification((String) name.getText());
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onDeleteClick(view, getAdapterPosition());
                }
            });

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void cancelNotification(String title) {
            NotificationManager manager = (NotificationManager) m_activity.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.deleteNotificationChannel(title);
        }


        void onBind(Schedule item) {
            name.setText(item.getName());
            String fromto = item.getStartTimeAsString() + " - " + item.getEndTimeAsString();
            time.setText(fromto);
            swtich.setChecked(item.getAlarm());
        }
    }
}
