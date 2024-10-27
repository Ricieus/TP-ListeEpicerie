package com.example.tp_listeepicerie

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
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
import kotlinx.coroutines.withContext

class PageFavorite : AppCompatActivity() {
    private lateinit var recyclerViewFav: RecyclerView

    private var listFavorite: MutableList<Table_Epicerie> = mutableListOf()
    private var genericList: MutableList<Table_Epicerie> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_page_favorite)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


//        listFavorite = mutableListOf(
//            Table_Epicerie(
//                0,
//                "Pomme",
//                1,
//                Uri.Builder().scheme("android.resource").authority(packageName)
//                    .appendPath(R.drawable.img.toString()).build().toString(),
//                "fruits",
//                "Lorem ipsum dolor sit amet, consectetur adipiscing elit," +
//                        " sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut" +
//                        " aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur" +
//                        " sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
//                false,
//                false
//            )
//        )

        initializeReycler()

        val database = Database_Epicerie.getDatabase(applicationContext)
        // https://stackoverflow.com/questions/3386667/query-if-android-database-exists
        lifecycleScope.launch(Dispatchers.IO) {
//            if ((applicationContext.getDatabasePath("epicerie_database")).exists()) {
//                for (grocery in listFavorite) {
//                    val existingItem = database.epicerieDao().findByName(grocery.nameProduct)
//
//                    if (existingItem != null) {
//                        if (existingItem.isFavorite) {
//                            val itemPanier = existingItem.copy(isCart = true, isFavorite = false)
//                            database.epicerieDao().updateEpicerie(itemPanier)
//
//                //                        withContext(Dispatchers.Main) {
//                //                            cartItems = database.epicerieDao().getAllPanier()
//                //                            groceryList = database.epicerieDao().getAllProduct()
//                //                            refreshRecyclerView()
//                //                        }
//                        }
//                    }
//
////                    withContext(Dispatchers.Main) {
////                        cartItems = database.epicerieDao().getAllPanier()
////                        groceryList = database.epicerieDao().getAllProduct()
////                        refreshRecyclerView()
////                    }
////
////                    if (existingItem == null) {
////                        database.epicerieDao().updateEpicerie(grocery)
////                    }
//                }
//            }

            listFavorite = database.epicerieDao().getAllFavoris()
            launch(Dispatchers.Main) {
                recyclerViewFav.adapter =
                    FavoriteAdaptor(applicationContext, this@PageFavorite, listFavorite)
            }
        }
    }

    private fun initializeReycler() {
        recyclerViewFav = findViewById(R.id.recyclerItemFavorite)
        recyclerViewFav.layoutManager = GridLayoutManager(this, 1)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.page_favorite_menu, menu)
        return super.onCreateOptionsMenu(
            menu
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.HomeButtonBack -> {
                finish()
            }

            R.id.ButtonFilter -> {
                showFilterMenu()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Référence : https://www.geeksforgeeks.org/popup-menu-in-android-with-example/
    private fun showFilterMenu() {
        val popupMenu = PopupMenu(this, findViewById(R.id.ButtonFilter))
        popupMenu.menuInflater.inflate(R.menu.filter_popup_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { selectedFilter ->
            when (selectedFilter.itemId) {
                R.id.filter_show_all -> applyFilter("Reaffichage des produits")
                R.id.filter_price_low_to_high -> applyFilter("Quantité: Peu à Beaucoup")
                R.id.filter_price_high_to_low -> applyFilter("Quantité: Beaucoup à Peu")
                R.id.filter_alphabetic_ordre_name -> applyFilter("Ordre en alphabetique (Par nom)")
                R.id.filter_alphabetic_ordre_category -> applyFilter("Ordre en alphabetique (Par catégorie)")
            }
            true
        }
        popupMenu.show()
    }

    private fun applyFilter(filter: String) {
        val filterList: MutableList<Table_Epicerie> = when (filter) {
            "Reaffichage des produits" -> listFavorite
            "Quantité: Peu à Beaucoup" -> listFavorite.sortedBy { it.quantity }.toMutableList()
            "Quantité: Beaucoup à Peu" -> listFavorite.sortedByDescending { it.quantity }
                .toMutableList()

            "Ordre en alphabetique (Par nom)" -> listFavorite.sortedBy { it.nameProduct }
                .toMutableList()

            "Ordre en alphabetique (Par catégorie)" -> listFavorite.sortedBy { it.category }
                .toMutableList()

            else -> listFavorite
        }

        lifecycleScope.launch(Dispatchers.IO) {
            launch(Dispatchers.Main) {
                recyclerViewFav.adapter =
                    FavoriteAdaptor(applicationContext, this@PageFavorite, filterList)
            }
        }
    }

    fun removeItemFromFavorite(currentItem: Table_Epicerie) {
        val database = Database_Epicerie.getDatabase(applicationContext)

        lifecycleScope.launch(Dispatchers.IO) {

            val itemProduct = Table_Epicerie(
                uid = currentItem.uid,
                nameProduct = currentItem.nameProduct,
                quantity = currentItem.quantity,
                foodImageURI = currentItem.foodImageURI,
                category = currentItem.category,
                description = currentItem.description,
                isCart = currentItem.isCart,
                isFavorite = false
            )
            database.epicerieDao().updateEpicerie(itemProduct)

            listFavorite = database.epicerieDao().getAllFavoris()
            launch(Dispatchers.Main) {
                recyclerViewFav.adapter =
                    FavoriteAdaptor(applicationContext, this@PageFavorite, listFavorite)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val database = Database_Epicerie.getDatabase(applicationContext)
        lifecycleScope.launch(Dispatchers.IO) {
            listFavorite = database.epicerieDao().getAllFavoris()
            launch(Dispatchers.Main) {
                recyclerViewFav.adapter =
                    FavoriteAdaptor(applicationContext, this@PageFavorite, listFavorite)
            }
        }
    }

}