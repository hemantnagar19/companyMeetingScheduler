package com.example.companymeetingscheduler

import com.google.gson.annotations.SerializedName

data class ScheduledSlotWrapper(
    @SerializedName("StartTime") var start_time :String,
    @SerializedName("EndTime") var end_time :String,
    @SerializedName("Description") var description :String,
    @SerializedName("Participants") var participants:List<String>
)

data class ScheduledMeetingResponse(
    @SerializedName("Response") var response : List<ScheduledSlotWrapper>
)