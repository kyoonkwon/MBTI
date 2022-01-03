package com.example.madcamp_pj1.ui.schedule;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcamp_pj1.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import nl.joery.timerangepicker.TimeRangePicker;

public class ScheduleFragment extends Fragment {

    private final Handler timerHandler = new Handler();
    Date currentTime;
    ArrayList<Schedule> schedules;
    ImageButton scheduleAddBtn;
    SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
    SimpleDateFormat minFormat = new SimpleDateFormat("mm");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    private Canvas SchedulerCanvas;
    private ImageView scheduler;
    private Bitmap schedulerBitmap;
    private RecyclerView rview;
    private ScheduleAdapter adapter;
    private TextView curScheduleView;
    private final int canvasWidth = 1024;
    private final int canvasHeight = 1024;
    private TimerTask timerTask;

    private void timerStart() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Update();
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 0, 10000);
    }

    protected void Update() {
        Runnable updater = new Runnable() {
            public void run() {
                currentTime = Calendar.getInstance().getTime();
                drawTimeBar();
                scheduler.setImageDrawable(new BitmapDrawable(getResources(), schedulerBitmap));
                findCurrentSchedule();
            }
        };
        timerHandler.post(updater);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timerTask.cancel();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getChildFragmentManager().setFragmentResultListener("dialog", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String name = result.getString("name");
                String startTime = result.getString("startTime");
                String endTime = result.getString("endTime");
                Boolean isAlarm = result.getBoolean("alarm");

                Log.i("log1", name + startTime + endTime + isAlarm);
                try {
                    schedules.add(new Schedule(name, sdf.parse(startTime), sdf.parse(endTime), isAlarm));
                    Collections.sort(schedules);
                    adapter.notifyDataSetChanged();
                    reDraw();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_schedule, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scheduler = view.findViewById(R.id.scheduleTimebar);
        schedulerBitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
        SchedulerCanvas = new Canvas(schedulerBitmap);
        curScheduleView = view.findViewById(R.id.scheduleCurrnet);
        currentTime = Calendar.getInstance().getTime();

        drawTimeBar();
        scheduler.setImageDrawable(new BitmapDrawable(getResources(), schedulerBitmap));
        timerStart();

        adapter = new ScheduleAdapter(getActivity());
        rview = view.findViewById(R.id.scheduleRecycler);
        rview.setAdapter(adapter);
        rview.setLayoutManager(new LinearLayoutManager(getActivity()));

        schedules = new ArrayList<>();

//        try {
//            schedules.add(new Schedule("잠자기", sdf.parse("00:00"), sdf.parse("11:00"), false));
//            schedules.add(new Schedule("점심먹기", sdf.parse("12:00"), sdf.parse("13:00"), true));
//            schedules.add(new Schedule("코딩하기", sdf.parse("14:00"), sdf.parse("18:00"), false));
//            schedules.add(new Schedule("저녁먹기",  sdf.parse("18:30"), sdf.parse("20:00"), true));
//            schedules.add(new Schedule("코딩또하기",  sdf.parse("20:00"), sdf.parse("23:59"), false));
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


        adapter.setScheduleList(schedules);

        int[] colors = {0xffffafb0, 0xffffafd8, 0xffeeb7b4, 0xfff2cfa5, 0xfffcffb0, 0xffaee4ff};
        int i = 0;
        for (Schedule schedule : schedules) {
            drawSchedule(schedule, colors[i]);
            if (sdf.format(currentTime).compareTo(sdf.format(schedule.getStartTime())) >= 0 &&
                    sdf.format(currentTime).compareTo(sdf.format(schedule.getEndTime())) < 0) {
                curScheduleView.setText(sdf.format(currentTime) + " - " + schedule.getName());
            }

            i = (i + 1) % colors.length;
        }

        for (Schedule schedule : schedules) {
            writeSchedule(schedule);
        }

        scheduleAddBtn = view.findViewById(R.id.scheduleAddBtn);
        scheduleAddBtn.setOnClickListener(v -> new DialogFragment().show(getChildFragmentManager(), "dialog"));


    }


    private void reDraw(){

        Paint paint = new Paint();
        paint.setColor(0xFFFFFFFF);
        SchedulerCanvas.drawCircle(canvasWidth/2, canvasHeight/2, canvasHeight/2-250, paint);


        int[] colors = {0xffffafb0, 0xffffafd8, 0xffeeb7b4, 0xfff2cfa5, 0xfffcffb0, 0xffaee4ff};
        int i=0;
        for(Schedule schedule : schedules){
            drawSchedule(schedule, colors[i]);
            if(sdf.format(currentTime).compareTo(sdf.format(schedule.getStartTime())) >= 0 &&
                    sdf.format(currentTime).compareTo(sdf.format(schedule.getEndTime())) < 0){
                curScheduleView.setText(sdf.format(currentTime) + " - " +schedule.getName());
            }

            i = (i + 1) % colors.length;
        }

        for(Schedule schedule: schedules){
            writeSchedule(schedule);
        }

    private void showAddSchedule() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_addschedule, null);
        builder.setView(view);
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "예를 선택했습니다.", Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "아니오를 선택했습니다.", Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();


    }

    private void findCurrentSchedule() {
        String curSchedule = "일정 없음";
        for (Schedule schedule : schedules) {
            if (sdf.format(currentTime).compareTo(sdf.format(schedule.getStartTime())) >= 0 &&
                    sdf.format(currentTime).compareTo(sdf.format(schedule.getEndTime())) < 0) {
                curSchedule = schedule.getName();
            }
        }

        curScheduleView.setText(sdf.format(currentTime) + " - " + curSchedule);

    }


    private void drawTimeBar() {

        int percent = (60 * Integer.parseInt(hourFormat.format((currentTime))) + Integer.parseInt(minFormat.format((currentTime)))) / 4;
        int barWidth = 60;

        Paint paint = new Paint();
        paint.setStrokeWidth(barWidth);
        paint.setStyle(Paint.Style.STROKE);

        Paint textPaint = new Paint();
        textPaint.setColor(0xFF000000);
        textPaint.setTextAlign(Paint.Align.CENTER);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            paint.setColor(0xFFEEEEEE);
            SchedulerCanvas.drawArc(barWidth + 40, barWidth + 40, canvasWidth - barWidth - 40, canvasHeight - barWidth - 40, -90, 360, false, paint);
            paint.setColor(0xFFFFC7C7);
            SchedulerCanvas.drawArc(barWidth + 20, barWidth + 20, canvasWidth - barWidth - 20, canvasHeight - barWidth - 20, -90, percent, false, paint);


            textPaint.setTextSize(80);
            for (int angle = 0; angle < 360; angle += 15) {
                double posx = canvasWidth / 2 + (canvasWidth / 2 - 90) * Math.cos(Math.toRadians(angle - 90));
                double posy = canvasHeight / 2 + (canvasHeight / 2 - 90) * Math.sin(Math.toRadians(angle - 90));

                if (angle % 90 == 0) {
                    textPaint.setTextSize(50);
                    SchedulerCanvas.drawText(String.valueOf(angle / 15), (int) posx, (int) posy + 20, textPaint);
                } else {
                    textPaint.setTextSize(50);
                    SchedulerCanvas.drawCircle((int) posx, (int) posy, 5, textPaint);
                }
            }
        }
    }

    private void drawSchedule(Schedule schedule, int color) {

        int strokeWidth = 10;
        int space = 150;

        Date startTime = schedule.getStartTime();
        Date endTime = schedule.getEndTime();

        int startAngle = (60 * Integer.parseInt(hourFormat.format((startTime))) + Integer.parseInt(minFormat.format((startTime)))) / 4;
        int endAngle = (60 * Integer.parseInt(hourFormat.format((endTime))) + Integer.parseInt(minFormat.format((endTime)))) / 4;

        Paint paint = new Paint();
        Paint strokePaint = new Paint();

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(color);

        strokePaint.setStrokeWidth(strokeWidth);
        strokePaint.setColor(0xFFFFC7C7);
        strokePaint.setStyle(Paint.Style.STROKE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SchedulerCanvas.drawArc(space, space, canvasWidth - space, canvasHeight - space, -90 + startAngle, endAngle - startAngle, true, paint);
            //SchedulerCanvas.drawArc(space, space, canvasWidth-space, canvasHeight-space, -90+startAngle, endAngle-startAngle, true, strokePaint);


        }

    }

    private void writeSchedule(Schedule schedule) {
        int fontSize = 50;
        int r = canvasWidth / 2 - 250;

        Date startTime = schedule.getStartTime();
        Date endTime = schedule.getEndTime();

        int startAngle = (60 * Integer.parseInt(hourFormat.format((startTime))) + Integer.parseInt(minFormat.format((startTime)))) / 4;
        int endAngle = (60 * Integer.parseInt(hourFormat.format((endTime))) + Integer.parseInt(minFormat.format((endTime)))) / 4;
        int middleAngle = (startAngle + endAngle) / 2;

        Paint textPaint = new Paint();
        textPaint.setColor(0xFF000000);
        textPaint.setTextSize(fontSize);
        textPaint.setTextAlign(Paint.Align.CENTER);

        Rect bounds = new Rect();
        textPaint.getTextBounds(schedule.getName(), 0, schedule.getName().length(), bounds);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            double posx = canvasWidth / 2 + r * Math.cos(Math.toRadians(middleAngle - 90));
            double posy = canvasHeight / 2 + r * Math.sin(Math.toRadians(middleAngle - 90));


            SchedulerCanvas.drawText(schedule.getName(), (int) posx, (int) posy + 10, textPaint);


        }
    }


}