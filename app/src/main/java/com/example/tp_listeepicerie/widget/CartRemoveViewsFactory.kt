package com.example.tp_listeepicerie.widget

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.tp_listeepicerie.Database_Epicerie
import com.example.tp_listeepicerie.R
import com.example.tp_listeepicerie.Table_Grocery
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking

class CartRemoteViewsFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {

    private var cartItems: List<Table_Grocery> = emptyList()
    private lateinit var auth: FirebaseAuth

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

    private fun loadCartItems() {
        auth = Firebase.auth
        val currentUser = auth.currentUser
        runBlocking {
            val database = Database_Epicerie.getDatabase(context)
            cartItems = database.GroceryDAO().getAllPanierUser(currentUser?.email.toString())
        }
    }
}
