package com.android.inventorytracker.services.notification

import android.annotation.SuppressLint
import android.content.Context
import com.android.inventorytracker.data.model.ItemModel
import java.time.LocalDate

fun inventoryNotifier(itemModels: List<ItemModel>, context: Context, today: LocalDate) {
    val zeroItems = itemModels.filter { it.totalSubUnit() == 0 }
    val expiredItems = itemModels.filter { it.nearestExpiryDate?.isBefore(today) ?: false }
    val lowItems = itemModels.filter { it.totalSubUnit() > 0 && it.totalUnit() <= it.item.unitThreshold * 0.20 }
    val expiringItems = itemModels.filter { model ->
        model.nearestExpiryDate?.let { date ->
            !date.isBefore(today) && !date.isAfter(today.plusDays(model.item.expiryThreshold.toLong()))
        } ?: false
    }

    notifyIfNotEmpty(
        context,
        items = expiredItems,
        channel = AppChannel.CRITICAL,
        title = "Urgent Inventory Alert",
        text = "[EXPIRED ITEMS] ${expiredItems.count()}",
        subtext = expiredItems.map { "${it.item.name}: expired on ${it.nearestExpiryFormatted}" },
        id = "expired".hashCode()
    )

    notifyIfNotEmpty(
        context,
        items = zeroItems,
        channel = AppChannel.CRITICAL,
        title = "Urgent Inventory Alert",
        text = "[OUT OF STOCK] ${zeroItems.count()}",
        subtext = zeroItems.map { "${it.item.name}: no stock available" },
        id = "out_of_stock".hashCode()
    )

    notifyIfNotEmpty(
        context,
        items = expiringItems,
        channel = AppChannel.WARNING,
        title = "Inventory Status Notice",
        text = "[EXPIRING SOON] ${expiringItems.count()}",
        subtext = expiringItems.map { "${it.item.name}: use before ${it.nearestExpiryFormatted}" },
        id = "expiring".hashCode()
    )

    notifyIfNotEmpty(
        context,
        items = lowItems,
        channel = AppChannel.WARNING,
        title = "Inventory Status Notice",
        text = "[LOW STOCK] ${lowItems.count()}",
        subtext = lowItems.map { "${it.item.name}: ${it.totalUnit()} remaining" },
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