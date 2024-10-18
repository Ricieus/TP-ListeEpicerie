//package com.example.tp_listeepicerie
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//
//@Database(entities = [Table_Epicerie::class], version = 1, exportSchema = false)
//abstract class Database_Epicerie: RoomDatabase() {
//    abstract fun epicerieDao(): EpicerieDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: Database_Epicerie? = null
//
//        fun getDatabase(ctx: Context): Database_Epicerie {
//            val tempInstance = INSTANCE
//            if (tempInstance != null) return tempInstance
//
//            synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    ctx.applicationContext,
//                    Database_Epicerie::class.java,
//                    "epicerie_database"
//                ).build()
//                INSTANCE = instance
//                return instance
//            }
//        }
//    }
//}