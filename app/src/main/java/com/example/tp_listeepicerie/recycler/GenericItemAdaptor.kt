package com.example.tp_listeepicerie.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.GenericItem
import com.example.tp_listeepicerie.GenericItemHolder
import com.example.tp_listeepicerie.MainActivity
import com.example.tp_listeepicerie.R

class GenericItemAdaptor(val ctx: Context, val activity: MainActivity, var data: List<GenericItem>) : RecyclerView.Adapter<GenericItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericItemHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.liste_epicerie_item, parent, false)
        return GenericItemHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: GenericItemHolder, position: Int) {
        val currentGenericItem = data[position]

        holder.textName.text = currentGenericItem.nom
        holder.textQuantity.text = currentGenericItem.quantite.toString()
        holder.img.setImageResource(currentGenericItem.image)
        holder.btn.setOnClickListener {
            currentGenericItem.quantite++
            holder.textQuantity.text = currentGenericItem.quantite.toString()
        }
    }
}