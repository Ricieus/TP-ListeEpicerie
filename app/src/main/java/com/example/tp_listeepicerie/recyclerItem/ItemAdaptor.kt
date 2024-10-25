package com.example.tp_listeepicerie.recyclerItem

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.PageDetails
import com.example.tp_listeepicerie.PageListe
import com.example.tp_listeepicerie.R
import com.example.tp_listeepicerie.Table_Epicerie
import kotlinx.android.parcel.Parcelize

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
        val currentItem = data[position]

        holder.textName.text = currentItem.nameProduct
        holder.textPrice.text = "Quantite: ${currentItem.quantity}"

        val imageUri = currentItem.FoodImageURI
        holder.img.setImageURI(Uri.parse(imageUri))

        holder.btnInformation.setOnClickListener {
            val intent = Intent(activity, PageDetails::class.java)
            val infoItem = InfoItem(
                currentItem.uid,
                currentItem.nameProduct,
                currentItem.price,
                currentItem.quantity,
                currentItem.FoodImageURI,
                currentItem.category,
                currentItem.description,
                currentItem.boutonPanier,
                currentItem.boutonInformation
                )
            intent.putExtra("InfoItem", infoItem)
            activity.startActivity(intent)
        }

        holder.btnPanier.setOnClickListener {
            activity.ajoutPanier(currentItem)
        }
    }
}

@Parcelize
class InfoItem(var uid: Int,
               var nameItem: String,
               var price: Double,
               var quantity: Int,
               var FoodImageURI: String,
               var category: String,
               var description: String,
               var boutonPanier: Int,
               var boutonInformation: Int) : Parcelable

// TODO: Add a new class to simplify the intent.putExtra into one line