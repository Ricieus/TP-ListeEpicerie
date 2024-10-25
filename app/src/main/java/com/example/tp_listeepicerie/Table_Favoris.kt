package com.example.tp_listeepicerie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Table_Favoris(
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,
    var favoriteProductName: String,
    var favoriteQuantity: Int,
    var favoriteFoodImage: String,
    var favoriteCategory: String,
    var favoriteDescription: String,
) {
//TODO: NEED TO DELETE THE BUTTON INSIDE THE TABLE AND THE PRICE
}
