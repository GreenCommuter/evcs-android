package org.evcs.android.util

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import android.appwidget.AppWidgetManager
import android.content.ComponentName

class MService : Service() {

    var receiver: BroadcastReceiver? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (receiver == null) {
            receiver = Receiver()
            Log.d("asd", "onstart")
            registerReceiver(receiver, IntentFilter(Intent.ACTION_TIME_TICK))
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    inner class Receiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("asd", intent?.action!!)
            if (intent?.action == Intent.ACTION_TIME_TICK) {
                val intentToSend = Intent(/*this, WidgetProvider::class.java*/)
                intentToSend.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
                // since it seems the onUpdate() is only fired on that:
                val ids = AppWidgetManager.getInstance(application)
                        .getAppWidgetIds(ComponentName(application, WidgetProvider::class.java))
                intentToSend.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                sendBroadcast(intentToSend)
            }
        }

    }

}
