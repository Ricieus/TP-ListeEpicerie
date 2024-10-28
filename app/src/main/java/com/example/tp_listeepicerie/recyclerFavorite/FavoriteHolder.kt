package com.example.tp_listeepicerie.recyclerFavorite

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.R

class FavoriteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val layout: ConstraintLayout
    val textName: TextView
    val textQuantity: TextView
    val img: ImageView
    val btnInformation: ImageButton
    val btnFavorite: ImageButton

    //Initialisation des variables
    init {
        layout = itemView as ConstraintLayout
        textName = itemView.findViewById(R.id.itemName)
        textQuantity = itemView.findViewById(R.id.itemQuantity)
        img = itemView.findViewById(R.id.itemImage)
        btnInformation = itemView.findViewById(R.id.btnInfo)
        btnFavorite = itemView.findViewById(R.id.btnFavorite)
    }
}