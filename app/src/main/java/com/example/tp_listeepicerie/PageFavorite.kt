package com.example.tp_listeepicerie

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.recyclerFavorite.FavoriteAdaptor
import com.example.tp_listeepicerie.recyclerItem.ItemAdaptor
import com.example.tp_listeepicerie.recyclerPanier.PanierAdaptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PageFavorite : AppCompatActivity() {
    private lateinit var recyclerViewFav : RecyclerView

    private var listFavorite : MutableList<Table_Favoris> = mutableListOf()
    private var genericList: MutableList<Table_Favoris> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_page_favorite)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listFavorite = mutableListOf(
            Table_Favoris(0,"Pomme", 1, Uri.Builder().scheme("android.resource").authority(packageName).appendPath(R.drawable.img.toString()).build().toString(), "fruits", "Lorem ipsum dolor sit amet, consectetur adipiscing elit," +
                    " sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut" +
                    " aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur" +
                    " sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
            )
        )

        recyclerViewFav = findViewById(R.id.recyclerItemFavorite)
        recyclerViewFav.layoutManager = GridLayoutManager(this, 1)
        val database = Database_Epicerie.getDatabase(applicationContext)
        // https://stackoverflow.com/questions/3386667/query-if-android-database-exists
        lifecycleScope.launch(Dispatchers.IO) {
            if ((applicationContext.getDatabasePath("epicerie_database")).exists()) {
                for (epicerie in listFavorite) {
                    val existingItem = database.epicerieDao().findByNameFav(epicerie.favoriteProductName)

                    if (existingItem == null) {
                        database.epicerieDao().insertEpicerieFavorite(epicerie)
                    }
                }
            }



            listFavorite = database.epicerieDao().getAllFavoris()
            Log.d("PageFavorite", "Items in Table_Favoris: ${listFavorite.size}")
            launch(Dispatchers.Main) {
                recyclerViewFav.adapter = FavoriteAdaptor(applicationContext, this@PageFavorite, listFavorite)
            }
        }
    }
}