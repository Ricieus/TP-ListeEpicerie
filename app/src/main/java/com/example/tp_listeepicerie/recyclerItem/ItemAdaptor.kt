package com.example.tp_listeepicerie.recyclerItem

import android.content.Context
import android.content.Intent
import android.graphics.pdf.PdfDocument.Page
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.GenericItem
import com.example.tp_listeepicerie.MainActivity
import com.example.tp_listeepicerie.PageDetails
import com.example.tp_listeepicerie.R
import com.example.tp_listeepicerie.Table_Epicerie

class ItemAdaptor(val ctx: Context, val activity: MainActivity, var data: MutableList<Table_Epicerie>) : RecyclerView.Adapter<ItemHolder>() {
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
//            val intent = Intent(activity, PageDetails::class.java)
//            activity.startActivity(intent)

              //Working code to open page details:
//            val intent = Intent(activity, PageDetails::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
//            activity.startActivity(intent)

            //Test code pour transfer info dans page details
            val intent = Intent(activity, PageDetails::class.java)
            intent.putExtra("nomProduit", currentGenericItem.nom)
            intent.putExtra("imageProduit", currentGenericItem.imageNourriture)
            intent.putExtra("productDescription", currentGenericItem.description)
            activity.startActivity(intent)

        }
        holder.btnPanier.setOnClickListener {
            //DO SOMETHING (AJOUTER PANIER)
            activity.ajoutPanier(currentGenericItem)
            activity.deleteProduct(currentGenericItem)
        }
    }
}