package com.example.madcamp_pj1.ui.schedule;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.madcamp_pj1.R;

import java.text.SimpleDateFormat;

import nl.joery.timerangepicker.TimeRangePicker;

public class DialogFragment extends androidx.fragment.app.DialogFragment {

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_addschedule, null);

        TimeRangePicker trp = view.findViewById(R.id.timePicker);
        TextView scheduleName = view.findViewById(R.id.dialogScheduleName);
        TextView startTimeView = view.findViewById(R.id.dialog_startTime);
        TextView endTimeView = view.findViewById(R.id.dialog_endTime);
        Switch alarm = view.findViewById(R.id.dialog_alarm);

        startTimeView.setText(sdf.format(trp.getStartTime().getCalendar().getTime()));
        endTimeView.setText(sdf.format(trp.getEndTime().getCalendar().getTime()));

        trp.setOnTimeChangeListener(new TimeRangePicker.OnTimeChangeListener() {

            @Override
            public void onStartTimeChange(@NonNull TimeRangePicker.Time time) {
                startTimeView.setText(sdf.format(time.getCalendar().getTime()));
            }

            @Override
            public void onEndTimeChange(@NonNull TimeRangePicker.Time time) {
                endTimeView.setText(sdf.format(time.getCalendar().getTime()));
            }

            @Override
            public void onDurationChange(@NonNull TimeRangePicker.TimeDuration timeDuration) {

            }
        });

        trp.setOnDragChangeListener(new TimeRangePicker.OnDragChangeListener() {
            @Override
            public boolean onDragStart(@NonNull TimeRangePicker.Thumb thumb) {
                return true;
            }

            @Override
            public void onDragStop(@NonNull TimeRangePicker.Thumb thumb) {
                TimeRangePicker.Time startTime = trp.getStartTime();
                TimeRangePicker.Time endTime = trp.getEndTime();


                if (sdf.format(startTime.getCalendar().getTime()).compareTo(sdf.format(endTime.getCalendar().getTime())) > 0) {

                    trp.setStartTimeMinutes(endTime.getTotalMinutes());
                    trp.setEndTimeMinutes(startTime.getTotalMinutes());

                    startTimeView.setText(sdf.format(trp.getStartTime().getCalendar().getTime()));
                    endTimeView.setText(sdf.format(trp.getEndTime().getCalendar().getTime()));


//                    Log.i("log1", "startTime " + sdf.format(trp.getStartTime().getCalendar().getTime()));
//                    Log.i("log1", "endTime " + sdf.format(trp.getEndTime().getCalendar().getTime()));
                }


            }
        });

        builder.setView(view);

        Button submit = view.findViewById(R.id.dialog_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scheduleName.getText().length() == 0) {
                    Toast.makeText(getContext(), "일정 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                Bundle data = new Bundle();
                data.putString("name", scheduleName.getText().toString());
                data.putString("startTime", startTimeView.getText().toString());
                data.putString("endTime", endTimeView.getText().toString());
                data.putBoolean("alarm", alarm.isChecked());

                getParentFragmentManager().setFragmentResult("dialog", data);
                getDialog().dismiss();
            }
        });

        Button cancelBtn = view.findViewById(R.id.dialog_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });

        return builder.create();

    }
}
