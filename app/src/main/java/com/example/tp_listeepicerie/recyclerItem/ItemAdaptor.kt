package com.example.tp_listeepicerie.recyclerItem

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.PageDetails
import com.example.tp_listeepicerie.R
import com.example.tp_listeepicerie.Table_Grocery
import com.example.tp_listeepicerie.fragment.list_product
import kotlinx.android.parcel.Parcelize

class ItemAdaptor(
    val ctx: Context,
    val activity: list_product,
    var data: List<Table_Grocery>
) : RecyclerView.Adapter<ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.list_grocery_item, parent, false)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val currentItem = data[position]

        holder.textName.text = currentItem.nameProduct
        holder.textPrice.text = "Quantite: ${currentItem.quantity}"

        val imageUri = currentItem.foodImageURI
        holder.img.setImageURI(Uri.parse(imageUri))

        changeIcon(holder, currentItem)

        holder.btnInformation.setOnClickListener {
            val infoItem = InfoItem(
                currentItem.uid,
                currentItem.nameProduct,
                currentItem.quantity,
                currentItem.foodImageURI,
                currentItem.category,
                currentItem.description
            )

            val intent = Intent(ctx, PageDetails::class.java).apply {
                putExtra("InfoItem", infoItem)
            }
            ctx.startActivity(intent)
        }

        //Permet d'ajouter un item au panier
        holder.btnPanier.setOnClickListener {
            activity.ajoutPanier(currentItem)
        }

        holder.favorite.setOnClickListener {
            if (!currentItem.isFavorite) {
                activity.addItemToFavorite(currentItem)
                currentItem.isFavorite = true
                holder.favorite.setImageResource(R.drawable.baseline_star_yellow_24)
            } else {
                activity.removeItemFromFavorite(currentItem)
                currentItem.isFavorite = false
                holder.favorite.setImageResource(R.drawable.baseline_star_24)
            }
        }
    }
}

//Permet de changer l'icone du bouton favorite
private fun changeIcon(holder: ItemHolder, currentItem: Table_Grocery) {
    if (currentItem.isFavorite) {
        holder.favorite.setImageResource(R.drawable.baseline_star_yellow_24)
    } else {
        holder.favorite.setImageResource(R.drawable.baseline_star_24)
    }
}


@Parcelize
class InfoItem(
    var uid: Int,
    var nameItem: String,
    var quantity: Int,
    var FoodImageURI: String,
    var category: String,
    var description: String
) : Parcelable
