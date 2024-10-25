package com.example.tp_listeepicerie.recyclerFavorite

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.PageListe
import com.example.tp_listeepicerie.Table_Epicerie
import com.example.tp_listeepicerie.recyclerItem.ItemHolder

class ItemAdaptor(
    val ctx: Context,
    val activity: PageListe,
    var data: MutableList<Table_Epicerie>
) : RecyclerView.Adapter<ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        TODO("Not yet implemented")
    }
}