package com.example.tp_listeepicerie.recyclerItem

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

class ItemAdaptor(
    val ctx: Context,
    val activity: PageListe,
    var data: MutableList<Table_Epicerie>
) : RecyclerView.Adapter<ItemHolder>() {
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

        val imageUri = currentGenericItem.imageNourriture
        if (imageUri != null) {
            holder.img.setImageURI(Uri.parse(imageUri))
        } else {
            holder.img.setImageResource(R.drawable.img)
        }

        holder.btnInformation.setOnClickListener {
            val intent = Intent(activity, PageDetails::class.java)
            intent.putExtra("productId", currentGenericItem.uid)
            intent.putExtra("nomProduit", currentGenericItem.nom)
            intent.putExtra("imageProduit", currentGenericItem.imageNourriture)
            intent.putExtra("productDescription", currentGenericItem.description)
            intent.putExtra("productCategory", currentGenericItem.categorie)
            intent.putExtra("productQuantity", currentGenericItem.quantite)
            activity.startActivity(intent)
        }

        holder.btnPanier.setOnClickListener {
            activity.ajoutPanier(currentGenericItem)
        }
    }
}
// TODO: Add a new class to simplify the intent.putExtra into one line