package com.example.tp_listeepicerie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Table_Epicerie(
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,
    var nameProduct: String,
    var quantity: Int,
    var foodImageURI: String,
    var category: String,
    var description: String
) {
    //TODO NEED TO DELETE THE BUTTON INSIDE THE TABLE AND THE PRICE
}
