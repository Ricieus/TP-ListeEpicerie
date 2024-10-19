package com.example.tp_listeepicerie

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EpicerieDao {
    @Query("SELECT * FROM Table_Epicerie")
    suspend fun getAll(): List<Table_Epicerie>

    @Query("SELECT * FROM Table_Epicerie WHERE uid IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<Table_Epicerie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpicerie(epicerie: Table_Epicerie)

    @Delete
    suspend fun deleteEpicerie(epicerie: Table_Epicerie)

    @Query("DELETE FROM Table_Epicerie")
    suspend fun clearTable()
}