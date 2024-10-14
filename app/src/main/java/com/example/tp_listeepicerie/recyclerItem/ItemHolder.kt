package com.example.tp_listeepicerie.recyclerItem

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.R

class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val layout: ConstraintLayout
    val textName: TextView
    val textPrice: TextView
    val img: ImageView
    val btnInformation: ImageButton
    val btnPanier: ImageButton

    init {
        layout = itemView as ConstraintLayout
        textName = itemView.findViewById(R.id.genericName)
        textPrice = itemView.findViewById(R.id.genericPrix)
        img = itemView.findViewById(R.id.genericImg)
        btnInformation = itemView.findViewById(R.id.btnInfo)
        btnPanier = itemView.findViewById(R.id.btnAjout)
    }
}