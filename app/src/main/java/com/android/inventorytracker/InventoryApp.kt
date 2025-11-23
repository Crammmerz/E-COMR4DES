package com.android.inventorytracker

import android.app.Application
import com.android.inventorytracker.services.notification.ChannelCreator
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class InventoryApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ChannelCreator.createAll(this)
    }
}
