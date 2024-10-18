//package com.example.tp_listeepicerie
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//
//class EpicerieDao {
//    @Dao
//    interface EpicerieDao {
//        @Query("SELECT * FROM Table_Epicerie.Epicerie")
//        suspend fun getAll(): List<Table_Epicerie.Epicerie>
//
//        @Query("SELECT * FROM Table_Epicerie.Epicerie WHERE uid IN (:userIds)")
//        suspend fun loadAllByIds(userIds: IntArray): List<Table_Epicerie.Epicerie>
//
//        @Insert(onConflict = OnConflictStrategy.REPLACE)
//        suspend fun insertClient(client: Table_Epicerie.Epicerie)
//    }
//
//}