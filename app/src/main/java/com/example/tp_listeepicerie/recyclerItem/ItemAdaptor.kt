package com.example.tp_listeepicerie.recyclerItem

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.GenericItem
import com.example.tp_listeepicerie.MainActivity
import com.example.tp_listeepicerie.R

class ItemAdaptor(val ctx: Context, val activity: MainActivity, var data: List<GenericItem>) : RecyclerView.Adapter<ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.liste_epicerie_item, parent, false)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val currentGenericItem = data[position]

        holder.textName.text = currentGenericItem.nom
        holder.textPrice.text = currentGenericItem.prix.toString() + "$"
        holder.img.setImageResource(currentGenericItem.imageNourriture)
        holder.btnInformation.setOnClickListener {
            //DO SOMETHING (NEW PAGE)
        }
        holder.btnPanier.setOnClickListener {
            //DO SOMETHING (AJOUTER PANIER)
            activity.addToCart(currentGenericItem)
        }
    }
}