package com.example.tp_listeepicerie.recyclerCart

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.page.PageDetails
import com.example.tp_listeepicerie.R
import com.example.tp_listeepicerie.Table_Grocery
import com.example.tp_listeepicerie.fragment.list_cart
import com.example.tp_listeepicerie.recyclerItem.InfoItem

class CartAdaptor(
    val ctx: Context,
    val activity: list_cart,
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

        //Permet de transferer les informations dans l'activité PageDetails
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

        //Permet de retirer un item du panier
        holder.btnPanier.setOnClickListener {
            activity.removeFromPanier(currentItem)
        }
    }
}