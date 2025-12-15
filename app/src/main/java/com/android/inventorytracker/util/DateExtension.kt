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

fun convertDaysToString(totalDays: Int): String {
    val months = totalDays / 30
    val remainingAfterMonths = totalDays % 30

    val weeks = remainingAfterMonths / 7
    val days = remainingAfterMonths % 7

    val parts = mutableListOf<String>()
    if (months > 0) parts.add("${months}m")
    if (weeks > 0) parts.add("${weeks}w")
    if (days > 0) parts.add("${days}d")

    return if (parts.isNotEmpty()) parts.joinToString(" ") else "0 days"
}