package com.example.tp_listeepicerie.recycler

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.GenericItem
import com.example.tp_listeepicerie.recycler.GenericItemHolder
import com.example.tp_listeepicerie.MainActivity
import com.example.tp_listeepicerie.PageDetails
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
        holder.textQuantity.text = currentGenericItem.prix.toString()
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