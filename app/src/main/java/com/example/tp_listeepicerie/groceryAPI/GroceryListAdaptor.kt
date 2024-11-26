package com.example.tp_listeepicerie.groceryAPI

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.R

class GroceryListAdaptor(
    val ctx: Context,
    var groceryList: List<GroceryItem>
): RecyclerView.Adapter<GroceryAPIHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryAPIHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.grocery_view_holder, parent, false)
        return GroceryAPIHolder(view)
    }

    override fun getItemCount(): Int {
        return groceryList.size
    }

    override fun onBindViewHolder(holder: GroceryAPIHolder, position: Int) {
        val grocery = groceryList[position]
        holder.name.text = grocery.name
        holder.category.text = grocery.categoryId

    }
}