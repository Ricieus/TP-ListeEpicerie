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

class ItemListWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return CartRemoteViewsFactory(applicationContext)
    }
}

class CartRemoteViewsFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {
    private var cartItems: List<Table_Grocery> = emptyList()

    override fun onCreate() {
        loadCartItems()
    }

    override fun onDataSetChanged() {
        loadCartItems()
    }

    override fun onDestroy() {}

    override fun getCount(): Int = cartItems.size

    override fun getViewAt(position: Int): RemoteViews {
        if (position >= cartItems.size) return RemoteViews(context.packageName, R.layout.widget_item)

        val item = cartItems[position]
        val views = RemoteViews(context.packageName, R.layout.widget_item)
        views.setTextViewText(R.id.item_name, item.nameProduct)
        views.setTextViewText(R.id.item_quantity, "Qty: ${item.quantity}")

        return views
    }

    override fun getLoadingView(): RemoteViews? = null
    override fun getViewTypeCount(): Int = 1
    override fun getItemId(position: Int): Long = position.toLong()
    override fun hasStableIds(): Boolean = true

    fun loadCartItems() {
        runBlocking {
            val database = Database_Epicerie.getDatabase(context)
            cartItems = database.GroceryDAO().getAllPanier()
        }
    }
}
