package com.example.companymeetingscheduler;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.companymeetingscheduler.retrofit.APIService;
import com.example.companymeetingscheduler.retrofit.ApiUtils;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShowScheduledMeetingActivity  extends AppCompatActivity {

    private Context context;
    private ArrayList<ScheduledSlotWrapper> listOfSlots;

    private LinearLayout ll_prev, ll_next;
    private TextView tv_date;
    private Button bt_schedule_meeting;
    private RecyclerView rcv_scheduled_meetingadapter;

    private ShowScheduledMeetingsAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        listOfSlots = new ArrayList<>();

        ll_prev = (LinearLayout) findViewById(R.id.llPrev);
        ll_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateValue = tv_date.getText().toString();
                Log.d("az date value prev clicked", dateValue);
                fetchScheduledSlotsList(Utils.getPrevDateSlash(Utils.dateConversion(dateValue, true)));
                tv_date.setText(Utils.getPrevDate(dateValue));
            }
        });


        ll_next = (LinearLayout) findViewById(R.id.llNext);
        ll_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateValue = tv_date.getText().toString();
                Log.d("az date value next clicked", dateValue);
                fetchScheduledSlotsList(Utils.getNextDateSlash(Utils.dateConversion(dateValue, true)));
                tv_date.setText(Utils.getNextDate(dateValue));
            }
        });

        tv_date = (TextView) findViewById(R.id.tv_date_title);

        rcv_scheduled_meetingadapter = (RecyclerView) findViewById(R.id.rcv_scheduled_meetings);

        rcv_scheduled_meetingadapter.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(context));

        tv_date.setText(Utils.getCurrentDate());

        tv_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Utils.isPastDate(tv_date.getText().toString())) {
                    bt_schedule_meeting.setEnabled(false);
                } else {
                    bt_schedule_meeting.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bt_schedule_meeting = (Button) findViewById(R.id.bt_schedule_meeting);
        bt_schedule_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateValue = tv_date.getText().toString();
                if(Utils.isPastDate(dateValue)) {
                    Toast.makeText(context, "Can't schedule meeting on past date", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "schedule company meeting", Toast.LENGTH_LONG).show();
                }
            }
        });

        fetchScheduledSlotsList(Utils.getCurrentDateSlash());

    }


    private void fetchScheduledSlotsList(String dateValue) {
        final APIService mAPIService = ApiUtils.getAPIService();
        Call call = mAPIService.fetchScheduledSlots(dateValue);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    Log.d("az response ", response.body().toString());
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        Log.d("az json", jsonArray.toString());

                        prepareScheduledSlotsList(sortJsonArrayByStartTime(jsonArray));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

    }

    /**
     * function to prepare the list of scheduled meeting slots and set adapter
     */
    private void prepareScheduledSlotsList(JSONArray response) {


        listOfSlots.clear();

        int i = 0;

        ArrayList<String> participantList = new ArrayList<>();
        while(i < response.length()) {
            try {
                participantList.clear();
                JSONArray participantArray = response.getJSONObject(i).getJSONArray("participants");
                for (int l = 0; l < participantArray.length(); l++) {
                    participantList.add(participantArray.getString(l));
                }

                String start_time = response.getJSONObject(i).getString("start_time");
                String end_time = response.getJSONObject(i).getString("end_time");
                listOfSlots.add(
                        new ScheduledSlotWrapper(
                                start_time,
                                end_time,
                                response.getJSONObject(i).getString("description"),
                                participantList
                        )
                );
            } catch (JSONException je) {
                je.printStackTrace();
            }
            i = i + 1;
        }

        setAdapter(listOfSlots);

    }


    /**
     * function to set adapter to scheduled slots recyclerview
     */
    private void setAdapter(List<ScheduledSlotWrapper> list) {
        adapter = new ShowScheduledMeetingsAdapter(context, list);
        rcv_scheduled_meetingadapter.setAdapter(adapter);
        if (list.size() > 0) {
            Log.d("az", "have booked slots");
        } else {
            Toast.makeText(context, "No booked slots", Toast.LENGTH_LONG).show();
        }

    }

    private JSONArray sortJsonArrayByStartTime(JSONArray jsonArray) {

        JSONArray sortedJsonArray = new JSONArray();

        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                jsonValues.add(jsonArray.getJSONObject(i));
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }
        Collections.sort( jsonValues, new Comparator<JSONObject>() {
            //Sort by start_time
            private static final String KEY_NAME = "start_time";

            @Override
            public int compare(JSONObject a, JSONObject b) {
                int valA = 0;
                int valB = 0;

                try {
                    valA =  Integer.parseInt(a.getString(KEY_NAME).split(":")[0]);
                    valB = Integer.parseInt(b.getString(KEY_NAME).split(":")[0]);
                }
                catch (JSONException e) {
                    //do something
                }

                return valA >= valB?1:-1;
                //if you want to change the sort order, simply use the following:
                //return  valA >= valB?-1:1;
            }
        });

        for (int i = 0; i < jsonArray.length(); i++) {
            sortedJsonArray.put(jsonValues.get(i));
        }

        return sortedJsonArray;

    }


}
