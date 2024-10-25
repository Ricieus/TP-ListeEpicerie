package com.example.tp_listeepicerie

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity
data class Table_Epicerie (
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,
    var nameProduct: String,
    var price: Double,
    var quantity: Int,
    var FoodImageURI: String,
    var category: String,
    var description: String,
    var boutonPanier: Int,
    var boutonInformation: Int
    ) {
    //TODO NEED TO DELETE THE BUTTON INSIDE THE TABLE AND THE PRICE
}
