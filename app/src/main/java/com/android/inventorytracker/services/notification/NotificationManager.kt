package com.android.inventorytracker.services.notification

import android.annotation.SuppressLint
import android.content.Context
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.util.toLocalDate
import java.time.LocalDate

fun inventoryNotifier(itemModels: List<ItemModel>, context: Context, today: LocalDate) {
    val isZero = itemModels.filter { it.totalSubUnit == 0 }
    val isExpired = itemModels.filter { it.nearestExpiry?.toLocalDate()?.isBefore(today) == true }
    val isLow = itemModels.filter { it.totalSubUnit > 0 && it.totalUnit <= it.item.unitThreshold * 0.20 }
    val isExpiring = itemModels.filter { it.nearestExpiry?.toLocalDate()?.let { date ->
        !date.isBefore(today) && date.isBefore(today.plusMonths(1))
    } == true }

    notifyIfNotEmpty(
        context,
        items = isExpired,
        channel = AppChannel.CRITICAL,
        title = "Urgent Inventory Alert",
        text = "[EXPIRED ITEMS] ${isExpired.count()}",
        subtext = isExpired.map { "${it.item.name}: expired on ${it.nearestExpiryFormatted()}" },
        id = "expired".hashCode()
    )

    notifyIfNotEmpty(
        context,
        items = isZero,
        channel = AppChannel.CRITICAL,
        title = "Urgent Inventory Alert",
        text = "[OUT OF STOCK] ${isZero.count()}",
        subtext = isZero.map { "${it.item.name}: no stock available" },
        id = "out_of_stock".hashCode()
    )

    notifyIfNotEmpty(
        context,
        items = isExpiring,
        channel = AppChannel.WARNING,
        title = "Inventory Status Notice",
        text = "[EXPIRING SOON] ${isExpiring.count()}",
        subtext = isExpiring.map { "${it.item.name}: use before ${it.nearestExpiryFormatted()}" },
        id = "expiring".hashCode()
    )

    notifyIfNotEmpty(
        context,
        items = isLow,
        channel = AppChannel.WARNING,
        title = "Inventory Status Notice",
        text = "[LOW STOCK] ${isLow.count()}",
        subtext = isLow.map { "${it.item.name}: ${it.totalUnit} remaining" },
        id = "low_stock".hashCode()
    )
}

@SuppressLint("MissingPermission")
private fun notifyIfNotEmpty(
    context: Context,
    items: List<ItemModel>,
    channel: AppChannel,
    title: String,
    text: String,
    subtext: List<String>,
    id: Int
) {
    if (items.isNotEmpty()) {
        val notif = NotificationHelper.buildNotification(
            context = context,
            channelId = channel.id,
            title = title,
            text = text,
            subtext = subtext,
            priority = NotificationHelper.channelImportanceToPriority(channel.importance),
        )
        NotificationHelper.safeNotify(context, id, notif)
    }
}