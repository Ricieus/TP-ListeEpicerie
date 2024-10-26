package com.example.tp_listeepicerie.recyclerFavorite

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.PageDetails
import com.example.tp_listeepicerie.PageFavorite
import com.example.tp_listeepicerie.PageListe
import com.example.tp_listeepicerie.R
import com.example.tp_listeepicerie.Table_Epicerie
import com.example.tp_listeepicerie.Table_Favoris
import com.example.tp_listeepicerie.recyclerItem.InfoItem
import com.example.tp_listeepicerie.recyclerItem.ItemHolder

class FavoriteAdaptor(
    val ctx: Context,
    val activity: PageFavorite,
    var data: MutableList<Table_Epicerie>
) : RecyclerView.Adapter<FavoriteHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.favorite_epicerie_item, parent, false)
        return FavoriteHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        val currentItem = data[position]

        holder.textName.text = currentItem.nameProduct
        holder.textPrice.text = "Quantite: ${currentItem.quantity}"

        val imageUri = currentItem.foodImageURI
        if (imageUri.isNotEmpty()) {
            holder.img.setImageURI(Uri.parse(imageUri))
        } else {
            holder.img.setImageResource(R.drawable.img)
        }

        holder.btnInformation.setOnClickListener {

        }

        holder.btnPanier.setOnClickListener {

        }
    }
}