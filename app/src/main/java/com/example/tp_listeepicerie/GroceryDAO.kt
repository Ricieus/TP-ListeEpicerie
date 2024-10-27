package com.example.tp_listeepicerie

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface GroceryDAO {

    @Query("SELECT * FROM Table_Grocery WHERE isCart = false")
    suspend fun getAllProduct(): MutableList<Table_Grocery>

    @Query("SELECT * FROM Table_Grocery WHERE nameProduct = :nom LIMIT 1")
    suspend fun findByName(nom: String): Table_Grocery?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpicerie(epicerie: Table_Grocery)

    @Delete
    suspend fun deleteEpicerie(epicerie: Table_Grocery)

    @Query("SELECT * FROM Table_Grocery WHERE isCart = true")
    suspend fun getAllPanier(): MutableList<Table_Grocery>

    @Update
    suspend fun updateEpicerie(item: Table_Grocery)

    @Query("SELECT * FROM Table_Grocery WHERE uid = :id")
    suspend fun getEpicerieId(id: Int): Table_Grocery?

    @Query("SELECT * FROM Table_Grocery WHERE isFavorite = true")
    suspend fun getAllFavoris(): MutableList<Table_Grocery>

    @Query("DELETE FROM Table_Grocery")
    suspend fun deleteAllInformation()

}