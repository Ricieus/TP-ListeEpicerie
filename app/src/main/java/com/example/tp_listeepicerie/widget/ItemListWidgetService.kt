package com.example.tp_listeepicerie.widget

import android.content.Intent
import android.widget.RemoteViewsService

class ItemListWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return CartRemoteViewsFactory(applicationContext)
    }
}
