package com.android.inventorytracker.workers

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.data.repository.ItemRepository
import com.android.inventorytracker.services.notification.AppChannel
import com.android.inventorytracker.services.notification.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.io.IOException
import java.time.LocalDate

@HiltWorker
class InventoryUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val itemRepository: ItemRepository
) : CoroutineWorker(context, params) {

    companion object {
        const val UNIQUE_NAME = "inventory_update_worker"
        const val INPUT_TITLE = "title"
        const val INPUT_TEXT = "text"
        const val NOTIF_ID = 2001
        private const val FG_NOTIF_ID = 2000
    }

    override suspend fun doWork(): Result {
        val context = applicationContext
        val canNotify = NotificationHelper.canPostNotifications(context)

        // optional foreground notification only when permission exists
        val fgNotif = if (canNotify) {
            NotificationHelper.buildNotification(
                context,
                AppChannel.INFO.id,
                title = "Syncing Inventory",
                text = "Checking stock and expiry",
                priority = NotificationHelper.channelImportanceToPriority(AppChannel.INFO.importance),
                ongoing = true
            )
        } else null

        fgNotif?.let {
            @SuppressLint("MissingPermission")
            try {
                setForegroundAsync(ForegroundInfo(FG_NOTIF_ID, it))
            } catch (e: Exception) {
                Log.d("WorkerTest", "Foreground failed")
            }
        }

        return try {

            Log.d("WorkerTest", "Worker started")
            val itemModels: List<ItemModel> = itemRepository.observeItemModels().first()
            val today = LocalDate.now()

            val isZero = itemModels.filter { it.totalSubUnit == 0 }
            val isExpired = itemModels.filter { it.nearestExpiry?.isBefore(today) == true }
            val isLow = itemModels.filter {
                it.totalSubUnit > 0 && it.totalSubUnit <= it.item.unitThreshold * 0.20
            }
            val isExpiring = itemModels.filter {
                it.nearestExpiry?.let { date ->
                    !date.isBefore(today) && date.isBefore(today.plusMonths(1))
                } == true
            }

            val expired = isExpired.joinToString { it.item.name }
            val noStock = isZero.joinToString { it.item.name }
            val lowStock = isLow.joinToString { it.item.name }
            val expiring = isExpiring.joinToString { it.item.name }

            if (canNotify) {
                when {
                    isZero.isNotEmpty() || isExpired.isNotEmpty() -> {
                        val contentText = buildString {
                            if (isExpired.isNotEmpty()) append("Expired Items: $expired\n")
                            if (isZero.isNotEmpty()) append("Out of Stock: $noStock\n")
                            if (isExpiring.isNotEmpty()) append("Nearly Expired Items: $expiring\n")
                            if (isLow.isNotEmpty()) append("Running Low Stock: $lowStock\n")
                        }

                        val notif = NotificationHelper.buildNotification(
                            context,
                            AppChannel.CRITICAL.id,
                            "Urgent Inventory Issue",
                            contentText,
                            NotificationHelper.channelImportanceToPriority(AppChannel.CRITICAL.importance)
                        )
                        @SuppressLint("MissingPermission")
                        NotificationHelper.safeNotify(context, "inventory_updates".hashCode(), notif)
                    }
                    isLow.isNotEmpty() || isExpiring.isNotEmpty() -> {
                        val contentText = buildString {
                            if(isExpiring.isNotEmpty()) append("Nearly Expired Items: $expiring\n")
                            if(isLow.isNotEmpty()) append("Running Low Stock: $lowStock\n")
                        }
                        val notif = NotificationHelper.buildNotification(
                            context,
                            AppChannel.WARNING.id,
                            "Inventory Status Notice",
                            contentText,
                            NotificationHelper.channelImportanceToPriority(AppChannel.WARNING.importance)
                        )
                        @SuppressLint("MissingPermission")
                        NotificationHelper.safeNotify(context, "inventory_updates".hashCode(), notif)
                    }
                }
            }


            Result.success()
        } catch (e: IOException) {
            Log.d("WorkerTest", "Worker started")
            Result.retry()
        } catch (e: Exception) {
            Log.e("WorkerTest", "Worker failed", e)
            Result.failure()
        }
    }
}
