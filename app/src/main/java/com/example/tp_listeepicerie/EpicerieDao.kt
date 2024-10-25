package com.example.tp_listeepicerie

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface EpicerieDao {
    @Query("SELECT * FROM Table_Epicerie")
    suspend fun getAll(): MutableList<Table_Epicerie>

    @Query("SELECT * FROM Table_Epicerie WHERE nameProduct = :nom LIMIT 1")
    suspend fun findByName(nom: String): Table_Epicerie?

    @Query("SELECT * FROM Table_Epicerie WHERE uid IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): MutableList<Table_Epicerie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpicerie(epicerie: Table_Epicerie)

    @Delete
    suspend fun deleteEpicerie(epicerie: Table_Epicerie)

    @Insert
    suspend fun insertPanier(epicerie: Table_Panier)

    @Query("DELETE FROM Table_Epicerie")
    suspend fun clearTable()

    @Query("SELECT * FROM Table_Panier")
    suspend fun getAllPanier(): MutableList<Table_Panier>

    @Delete
    suspend fun deleteEpiceriePanier(epicerie: Table_Panier)

    @Insert
    suspend fun insertProductList(epicerie: Table_Epicerie)

    @Update
    suspend fun updateEpicerie(item: Table_Epicerie)

    @Query("SELECT * FROM Table_Epicerie WHERE uid = :id")
    suspend fun getEpicerieId(id: Int): Table_Epicerie?

    @Query("SELECT * FROM Table_Favoris")
    suspend fun getAllFavoris(): MutableList<Table_Favoris>

    @Query("SELECT * FROM Table_Favoris WHERE favoriteProductName = :nom LIMIT 1")
    suspend fun findByNameFav(nom: String): Table_Favoris?

    @Insert
    suspend fun insertEpicerieFavorite(epicerie: Table_Favoris)
}