package com.example.tp_listeepicerie.recyclerPanier

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.PageListe
import com.example.tp_listeepicerie.R
import com.example.tp_listeepicerie.Table_Epicerie

class PanierAdaptor(
    val ctx: Context,
    val activity: PageListe,
    var data: MutableList<Table_Epicerie>
) : RecyclerView.Adapter<PanierHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PanierHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.panier_epicerie_item, parent, false)
        return PanierHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PanierHolder, position: Int) {
        val currentGenericItem = data[position]

        holder.textName.text = currentGenericItem.nameProduct
        holder.textPrice.text = "Quantite: ${currentGenericItem.quantity}"

        val imageUri = currentGenericItem.foodImageURI
        if (imageUri != null) {
            holder.img.setImageURI(Uri.parse(imageUri))
        } else {
            holder.img.setImageResource(R.drawable.img)
        }

//        holder.btnInformation.setOnClickListener {
//            //DO SOMETHING (NEW PAGE)
//        }
        holder.btnPanier.setOnClickListener {
            activity.removeFromPanier(currentGenericItem)
        }
    }
}