package com.example.companymeetingscheduler

import android.app.Application

class CompanyMeetingScheduler: Application() {

    /**
     * Makes this class a singleton. Easy to access this way. No messy globals.
     */
    lateinit var singleton: CompanyMeetingScheduler

    /**
     * Getter for the singleton reference.
     *
     * @return the only instance of the application
     */
    fun getInstance(): CompanyMeetingScheduler {
        return singleton
    }

    override fun onCreate() {
        super.onCreate()
        singleton = this
    }
}