package com.example.tp_listeepicerie.recyclerPanier

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.GenericItem
import com.example.tp_listeepicerie.MainActivity
import com.example.tp_listeepicerie.PageDetails
import com.example.tp_listeepicerie.R
import com.example.tp_listeepicerie.recyclerItem.ItemHolder

class PanierAdaptor(val ctx: Context, val activity: MainActivity, var data: List<GenericItem>) : RecyclerView.Adapter<ItemHolder>() {
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
            val intent = Intent(activity, PageDetails::class.java)
            activity.startActivity(intent)
        }
        holder.btnPanier.setOnClickListener {
            //DO SOMETHING (AJOUTER PANIER)
        }
    }
}