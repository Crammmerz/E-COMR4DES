package com.android.inventorytracker.services.notification

import android.app.NotificationManager
import androidx.annotation.StringRes
import com.android.inventorytracker.R

enum class AppChannel(
    val id: String,
    @StringRes val nameRes: Int,
    @StringRes val descRes: Int,
    val importance: Int
) {
    CRITICAL("app_channel_critical", R.string.channel_name_critical, R.string.channel_desc_critical, NotificationManager.IMPORTANCE_HIGH),
    WARNING ("app_channel_warning",  R.string.channel_name_warning,  R.string.channel_desc_warning,  NotificationManager.IMPORTANCE_DEFAULT),
    INFO    ("app_channel_info",     R.string.channel_name_info,     R.string.channel_desc_info,     NotificationManager.IMPORTANCE_LOW),
    SILENT  ("app_channel_silent",   R.string.channel_name_silent,   R.string.channel_desc_silent,   NotificationManager.IMPORTANCE_MIN)
}
