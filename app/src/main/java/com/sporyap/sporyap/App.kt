package com.sporyap.sporyap

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application(){

    override fun onCreate() {
        super.onCreate()
        FacebookSdk.fullyInitialize()
        AppEventsLogger.activateApp(this)
    }
}