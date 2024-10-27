package com.example.tp_listeepicerie.recyclerCart

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.PageDetails
import com.example.tp_listeepicerie.PageList
import com.example.tp_listeepicerie.R
import com.example.tp_listeepicerie.Table_Grocery
import com.example.tp_listeepicerie.recyclerItem.InfoItem

class CartAdaptor(
    val ctx: Context,
    val activity: PageList,
    var data: MutableList<Table_Grocery>
) : RecyclerView.Adapter<CartHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.cart_grocery_item, parent, false)
        return CartHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CartHolder, position: Int) {
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