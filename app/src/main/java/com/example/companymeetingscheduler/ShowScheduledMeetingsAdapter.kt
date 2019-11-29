package com.example.companymeetingscheduler

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.companymeetingscheduler.Utils.convertTimeIntoAMPM
import kotlinx.android.synthetic.main.layout_scheduled_meeting_item.view.*


/**
 * adapter to show list of scheduled meeting slots
 */
class ShowScheduledMeetingsAdapter constructor(val context: Context, var listOfSlots: List<ScheduledSlotWrapper>) : androidx.recyclerview.widget.RecyclerView.Adapter<ShowScheduledMeetingsAdapter.viewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewHolder {
        /**
         * inflating ui for child of recylerview
         */
        var view = LayoutInflater.from(context).inflate(R.layout.layout_scheduled_meeting_item, p0, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfSlots.size
    }

    override fun onBindViewHolder(p0: viewHolder, p1: Int) {
        var slot = listOfSlots.get(p1)

        p0.mTvStartTime.text = convertTimeIntoAMPM(slot.start_time)
        p0.mTvEndTime.text = convertTimeIntoAMPM(slot.end_time)
        p0.mDescription.text = slot.description

        var participantsString = ""
        for (s in slot.participants) {
            participantsString +=  s + ","
        }
        p0.mParticipants.text = participantsString.removeSuffix(",")
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            p0.mParticipants.visibility = View.GONE
        } else {
            p0.mParticipants.visibility = View.VISIBLE
        }
    }


    /**
     * view holder with UI
     */
    class viewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var mTvStartTime = itemView.tv_booked_slot_start_time
        var mTvEndTime = itemView.tv_booked_slot_end_time
        var mDescription = itemView.slot_description
        var mParticipants = itemView.tvParticipants
    }

    /**
     * function to update the list of adapter to update UI
     */
    fun filterList(list: List<ScheduledSlotWrapper>) {
        this.listOfSlots = list
        notifyDataSetChanged()
    }
}