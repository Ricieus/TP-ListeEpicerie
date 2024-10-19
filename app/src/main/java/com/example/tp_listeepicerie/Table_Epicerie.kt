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
    var nom: String,
    var prix: Double,
    var quantite: Int,
    var imageNourriture: Int,
    var categorie: String,
    var description: String,
    var boutonPanier: Int,
    var boutonInformation: Int
    ) {

}
