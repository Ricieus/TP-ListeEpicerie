package com.example.tp_listeepicerie.recyclerFavorite

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.page.PageDetails
import com.example.tp_listeepicerie.page.PageFavorite
import com.example.tp_listeepicerie.R
import com.example.tp_listeepicerie.Table_Grocery
import com.example.tp_listeepicerie.recyclerItem.InfoItem

class FavoriteAdaptor(
    val ctx: Context,
    val activity: PageFavorite,
    var data: MutableList<Table_Grocery>
) : RecyclerView.Adapter<FavoriteHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.favorite_grocery_item, parent, false)
        return FavoriteHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        val currentItem = data[position]

        holder.textName.text = currentItem.nameProduct
        holder.textQuantity.text = "Quantite: ${currentItem.quantity}"

        //Permet d'afficher l'image
        val imageUri = currentItem.foodImageURI
        if (imageUri.isNotEmpty()) {
            holder.img.setImageURI(Uri.parse(imageUri))
        } else {
            holder.img.setImageResource(R.drawable.img)
        }

        holder.btnFavorite.setImageResource(R.drawable.baseline_star_yellow_24)

        //Permet de transferer les informations dans l'activité PageDetails
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

        //Permet de retirer un item des favoris
        holder.btnFavorite.setOnClickListener {
            activity.removeItemFromFavorite(currentItem)
        }
    }
}