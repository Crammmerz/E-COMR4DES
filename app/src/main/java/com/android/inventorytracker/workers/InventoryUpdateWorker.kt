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
import com.android.inventorytracker.services.notification.inventoryNotifier
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

            if (canNotify) {
                inventoryNotifier(itemModels, context, LocalDate.now())
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

