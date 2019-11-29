package com.example.companymeetingscheduler;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ScheduleMeetingActivity extends AppCompatActivity {

    private Context context;

    private LinearLayout llBack;

    private TextView etMeetingDate;

    private TextView etStartTime, etEndTime;

    private String selectedDate;

    private Button btSubmit;

    private JSONArray slotJsonArray;
    private ArrayList<ScheduledSlotWrapper> listOfSlots;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_meeting);
        context = this;

        if (getIntent().getExtras() != null) {
            selectedDate = getIntent().getExtras().getString("SelectedDate");
            try {
                slotJsonArray = new JSONArray((getIntent().getExtras().getString("SlotsJsonArray")));
            } catch (JSONException je) {
                je.printStackTrace();
            }

        }


        Log.d("az selected date", selectedDate);

        llBack = (LinearLayout) findViewById(R.id.llBack);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etMeetingDate = (TextView) findViewById(R.id.etMeetingDate);
        etMeetingDate.setText(selectedDate);
        etMeetingDate.setEnabled(false);

        etStartTime = (TextView) findViewById(R.id.etStartTime);
        etEndTime = (TextView) findViewById(R.id.etEndTime);

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String timestamp = simpleDateFormat.format(myCalendar.getTime());
                etMeetingDate.setText(timestamp);

            }

        };

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                String timestamp = simpleDateFormat.format(myCalendar.getTime());
                etStartTime.setText(timestamp);
            }
        };

        final TimePickerDialog.OnTimeSetListener time1 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                String timestamp = simpleDateFormat.format(myCalendar.getTime());
                etEndTime.setText(timestamp);
            }
        };

        etMeetingDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                selectedDate = etMeetingDate.getText().toString().trim();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date d = null;
                try {
                    d = simpleDateFormat.parse(selectedDate);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }

                myCalendar.setTime(d);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });

        etStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startTime = etStartTime.getText().toString().trim();
                if (startTime.equalsIgnoreCase("")) {
                    startTime = "00:00";
                }
                selectedDate = etMeetingDate.getText().toString().trim() ;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                Date d = null;
                try {
                    d = simpleDateFormat.parse(selectedDate +  " " + startTime);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }

                myCalendar.setTime(d);

                new TimePickerDialog(context, time, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });

        etEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endTime = etEndTime.getText().toString().trim();
                if (endTime.equalsIgnoreCase("")) {
                    endTime = "00:00";
                }
                selectedDate = etMeetingDate.getText().toString().trim() ;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                Date d = null;
                try {
                    d = simpleDateFormat.parse(selectedDate +  " " + endTime);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }

                myCalendar.setTime(d);

                new TimePickerDialog(context, time1, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });

        btSubmit = (Button) findViewById(R.id.bt_submit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startTime = etStartTime.getText().toString().trim();
                String endTime = etEndTime.getText().toString().trim();
                if (!startTime.equalsIgnoreCase("")) {
                    if (!endTime.equalsIgnoreCase("")) {
                        if (!isSlotAvailable(startTime, endTime)) {
                            Toast.makeText(context, "Slot available", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Slot not available", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Select end time", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Select start time", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private boolean isSlotAvailable ( String startTime, String endTime) {

        for (int i = 0; i < slotJsonArray.length(); i++) {
            try {
                String slotStartTime = slotJsonArray.getJSONObject(i).getString("start_time");
                String slotEndTime = slotJsonArray.getJSONObject(i).getString("end_time");
                if (isTimeExistBetween(slotStartTime, slotEndTime, startTime, endTime)) {
                    return true;
                }
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }

        return false;
    }

    private boolean isTimeExistBetween(String startTime, String endTime, String time1, String time2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        try {
            Date d1 = simpleDateFormat.parse(startTime);
            Date d2 = simpleDateFormat.parse(endTime);
            Date dStart = simpleDateFormat.parse(time1);
            Date dEnd = simpleDateFormat.parse(time2);

            if ((dStart.getTime() - d1.getTime() > 0 && dStart.getTime() - d2.getTime() < 0) || (dEnd.getTime() - d1.getTime() > 0 && dEnd.getTime() - d2.getTime() < 0)) {
                return true;
            } else {
                return false;
            }

        } catch (ParseException pe) {
            pe.printStackTrace();
        }


        return  false;

    }


}
