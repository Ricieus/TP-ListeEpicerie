package com.example.tp_listeepicerie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Table_Grocery(
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,
    var nameProduct: String,
    var quantity: Int,
    var foodImageURI: String,
    var category: String,
    var description: String,
    var isCart: Boolean,
    var isFavorite: Boolean,
    var currentUser: String
) {
}
