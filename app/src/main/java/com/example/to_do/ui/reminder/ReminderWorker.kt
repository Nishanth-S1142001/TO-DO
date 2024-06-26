package com.example.to_do.ui.reminder

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class ReminderWorker ( private val context: Context, params : WorkerParameters):Worker(context,params)
{
    override fun doWork(): Result {
        //show notification
        NotificationHelper(context).createNotification(
            inputData.getString("Title").toString(),
            inputData.getString("Message").toString()
        )
            return Result.success()
    }

}