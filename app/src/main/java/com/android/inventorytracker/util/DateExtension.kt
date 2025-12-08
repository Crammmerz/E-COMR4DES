package com.android.inventorytracker.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

fun Long.toFormattedDateString(
    formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
): String {
    val localDate = this.toLocalDate()
    return formatter.format(localDate)
}