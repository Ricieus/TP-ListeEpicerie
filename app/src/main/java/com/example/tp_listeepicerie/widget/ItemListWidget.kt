package com.example.tp_listeepicerie.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.tp_listeepicerie.Database_Epicerie
import com.example.tp_listeepicerie.PageDetails
import com.example.tp_listeepicerie.R
import com.example.tp_listeepicerie.Table_Grocery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

// https://www.geeksforgeeks.org/how-to-create-buttons-inside-a-widget-in-android/
class ItemListWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // When the last widget is enabled
    }

    override fun onDisabled(context: Context) {
        // When the last widget is disabled
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE == intent.action) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(context, ItemListWidget::class.java)
            )
            onUpdate(context, appWidgetManager, appWidgetIds)

            CartRemoteViewsFactory(context).onDataSetChanged()
        }
    }

    companion object {
        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val intent = Intent(context, ItemListWidgetService::class.java)
            val views = RemoteViews(context.packageName, R.layout.item_list_widget)
            views.setRemoteAdapter(R.id.cart_list_view, intent)

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.cart_list_view)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

