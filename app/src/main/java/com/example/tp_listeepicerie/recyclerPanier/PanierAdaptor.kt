package com.example.tp_listeepicerie.recyclerPanier

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.MainActivity
import com.example.tp_listeepicerie.R
import com.example.tp_listeepicerie.Table_Panier

class PanierAdaptor(val ctx: Context, val activity: MainActivity, var data: MutableList<Table_Panier>) : RecyclerView.Adapter<PanierHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PanierHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.panier_epicerie_item, parent, false)
        return PanierHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PanierHolder, position: Int) {
        val currentGenericItem = data[position]

        holder.textName.text = currentGenericItem.nom
        holder.textPrice.text = currentGenericItem.quantite.toString() + "$"

        val imageUri = currentGenericItem.imageNourriture
        if (imageUri != null) {
            holder.img.setImageURI(Uri.parse(imageUri))
        } else {
            holder.img.setImageResource(R.drawable.img)
        }

//        holder.btnInformation.setOnClickListener {
//            //DO SOMETHING (NEW PAGE)
//        }
        holder.btnPanier.setOnClickListener {
            //DO SOMETHING (AJOUTER PANIER)
            activity.removeFromPanier(currentGenericItem)
        }
    }
}