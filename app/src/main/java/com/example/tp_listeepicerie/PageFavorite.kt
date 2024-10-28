package com.example.tp_listeepicerie

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PageFavorite : AppCompatActivity() {
    private lateinit var recyclerViewFav: RecyclerView

    private var listFavorite: MutableList<Table_Grocery> = mutableListOf()

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

        initializeReycler()
        loadFavorite()

    }

    //Permet de charger les favoris
    private fun loadFavorite(){
        val database = Database_Epicerie.getDatabase(applicationContext)
        // https://stackoverflow.com/questions/3386667/query-if-android-database-exists
        lifecycleScope.launch(Dispatchers.IO) {

            listFavorite = database.GroceryDAO().getAllFavoris()
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

    //Permet d'afficher le menu de filtre
    // Référence : https://www.geeksforgeeks.org/popup-menu-in-android-with-example/
    private fun showFilterMenu() {
        val popupMenu = PopupMenu(this, findViewById(R.id.ButtonFilter))
        popupMenu.menuInflater.inflate(R.menu.filter_popup_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { selectedFilter ->
            when (selectedFilter.itemId) {
                R.id.filter_show_all -> applyFilter("Réaffichage des produits")
                R.id.filter_price_low_to_high -> applyFilter("Quantité: Peu à Beaucoup")
                R.id.filter_price_high_to_low -> applyFilter("Quantité: Beaucoup à Peu")
                R.id.filter_alphabetic_ordre_name -> applyFilter("Ordre en alphabetique (Par nom)")
                R.id.filter_alphabetic_ordre_category -> applyFilter("Ordre en alphabetique (Par catégorie)")
            }
            true
        }
        popupMenu.show()
    }


    //Permet d'appliquer le filtre
    private fun applyFilter(filter: String) {
        //Aidé par ChatGPT
        val filterList: MutableList<Table_Grocery> = when (filter) {
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

    fun removeItemFromFavorite(currentItem: Table_Grocery) {
        val database = Database_Epicerie.getDatabase(applicationContext)

        lifecycleScope.launch(Dispatchers.IO) {

            val itemProduct = Table_Grocery(
                uid = currentItem.uid,
                nameProduct = currentItem.nameProduct,
                quantity = currentItem.quantity,
                foodImageURI = currentItem.foodImageURI,
                category = currentItem.category,
                description = currentItem.description,
                isCart = currentItem.isCart,
                isFavorite = false
            )
            database.GroceryDAO().updateEpicerie(itemProduct)

            listFavorite = database.GroceryDAO().getAllFavoris()
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
            listFavorite = database.GroceryDAO().getAllFavoris()
            launch(Dispatchers.Main) {
                recyclerViewFav.adapter =
                    FavoriteAdaptor(applicationContext, this@PageFavorite, listFavorite)
            }
        }
    }

}