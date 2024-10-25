package com.example.tp_listeepicerie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Table_Panier(
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,
    var cartProductName: String,
    var cartProductPrice: Double,
    var cartQuantity: Int,
    var cartFoodImage: String,
    var cartCategory: String,
    var cartDescription: String,
    var cartbutton: Int,
    var cartInformation: Int
) {
//TODO: NEED TO DELETE THE BUTTON INSIDE THE TABLE AND THE PRICE
}
