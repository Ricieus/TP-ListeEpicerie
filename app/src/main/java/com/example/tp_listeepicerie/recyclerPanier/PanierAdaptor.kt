package com.example.tp_listeepicerie.recyclerPanier

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.PageDetails
import com.example.tp_listeepicerie.PageListe
import com.example.tp_listeepicerie.R
import com.example.tp_listeepicerie.Table_Epicerie
import com.example.tp_listeepicerie.recyclerItem.InfoItem

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
        val currentItem = data[position]

        holder.textName.text = currentItem.nameProduct
        holder.textPrice.text = "Quantite: ${currentItem.quantity}"

        val imageUri = currentItem.foodImageURI
        if (imageUri != null) {
            holder.img.setImageURI(Uri.parse(imageUri))
        } else {
            holder.img.setImageResource(R.drawable.img)
        }

        holder.btnInformation.setOnClickListener {
            val intent = Intent(activity, PageDetails::class.java)
            val infoItem = InfoItem(
                currentItem.uid,
                currentItem.nameProduct,
                currentItem.quantity,
                currentItem.foodImageURI,
                currentItem.category,
                currentItem.description
            )
            intent.putExtra("InfoItem", infoItem)
            activity.startActivity(intent)
        }
        holder.btnPanier.setOnClickListener {
            //DO SOMETHING (AJOUTER PANIER)
            activity.removeFromPanier(currentItem)
        }
    }
}