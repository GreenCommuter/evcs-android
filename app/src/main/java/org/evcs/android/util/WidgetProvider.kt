package org.evcs.android.util

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.*
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.*
import android.app.PendingIntent
import android.content.Intent
import org.evcs.android.R

/**
 * Class that controls all widgets provided by this app
 */
class WidgetProvider : AppWidgetProvider() {

    companion object {

        private val dateFormat = SimpleDateFormat("EEE dd MMM")
        private val timeFormat = SimpleDateFormat("H:mm")
        //These depend on the format
        private val TIME_BOLD_START = 0
        private val DATE_BOLD_START = 3
        private val DATE_BOLD_END = 6
    }

    /**
     * Called every time "updatePeriodMillis" milliseconds elapse, specified on widget_provider_info.xml,
     * though no more frequently than once every half an hour. Also called when one of this app's
     * widgets is added to the home screen.
     * @param context the context
     * @param appWidgetManager a manager that will be called to update the views
     * @param appWidgetIds an array with the ids of the widgets
     */
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager,
                          appWidgetIds: IntArray) {

        context.startService(Intent(context, MService::class.java))

        updateWidget(context, appWidgetIds, appWidgetManager)
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    private fun updateWidget(context: Context, id: IntArray, appWidgetManager: AppWidgetManager) {
        Log.d("asd", "updating")
        val views = RemoteViews(context.packageName, R.layout.widget_layout)

        val now = Date()
        val time = timeFormat.format(now)
        //Using textAllCaps conflicts with bold typeface
        val date = dateFormat.format(now).replace(".", "").toUpperCase()

        val timeBoldEnd = if (now.hours >= 10) 2 else 1
        views.setTextViewText(R.id.text_time, toBold(time, TIME_BOLD_START, timeBoldEnd))
        views.setTextViewText(R.id.text_date, toBold(date, DATE_BOLD_START, DATE_BOLD_END))

        Log.d("asd", "sending")
        appWidgetManager.partiallyUpdateAppWidget(id, views)
        Log.d("asd", "sent")
        // Update on UI thread
//        Handler(Looper.getMainLooper()).post {  }
    }

    private fun toBold(text: String, start: Int, end: Int): SpannableString {
        val res = SpannableString(text)
        res.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return res
    }

}
