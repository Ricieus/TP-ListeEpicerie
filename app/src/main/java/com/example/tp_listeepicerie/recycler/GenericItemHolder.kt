package com.example.tp_listeepicerie.recycler

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.R

class GenericItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val layout: ConstraintLayout
    val textName: TextView
    val textQuantity: TextView
    val img: ImageView
    val btnInformation: Button
    val btnPanier: Button

    init {
        layout = itemView as ConstraintLayout
        textName = itemView.findViewById(R.id.genericName)
        textQuantity = itemView.findViewById(R.id.genericQuantity)
        img = itemView.findViewById(R.id.genericImg)
        btnInformation = itemView.findViewById(R.id.genericBtn)
        btnPanier = itemView.findViewById(R.id.buttonAcheter)
    }
}