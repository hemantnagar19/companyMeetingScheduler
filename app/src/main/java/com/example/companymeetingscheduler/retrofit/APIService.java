package com.example.companymeetingscheduler.retrofit;
;

import com.example.companymeetingscheduler.ScheduledMeetingResponse;
import com.example.companymeetingscheduler.ScheduledSlotWrapper;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface APIService {

//    @GET("schedule")
//    @Headers("Content-Type: application/json")
//    Call<ScheduledMeetingResponse>fetchScheduledSlots(@Query("date") String dateValue);


    @GET("schedule")
    @Headers("Content-Type: application/json")
    Call<Object>fetchScheduledSlots(@Query("date") String dateValue);
}

