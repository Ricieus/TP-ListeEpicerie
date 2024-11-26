package com.example.tp_listeepicerie.groceryAPI

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.R

class GroceryAPIHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val layout: ConstraintLayout
    val name: TextView
    val category: TextView

    init {
        layout = itemView as ConstraintLayout
        name = itemView.findViewById(R.id.viewHolderName)
        category = itemView.findViewById(R.id.viewHolderCategory)
    }
}