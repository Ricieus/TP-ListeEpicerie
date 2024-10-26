package com.example.tp_listeepicerie

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.recyclerItem.ItemAdaptor
import com.example.tp_listeepicerie.recyclerPanier.PanierAdaptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PageListe : AppCompatActivity(){
    private var genericList: MutableList<Table_Epicerie> = mutableListOf()
    private var cartItems: MutableList<Table_Epicerie> = mutableListOf()
    private lateinit var recyclerViewCart: RecyclerView
    private lateinit var recyclerView: RecyclerView
    private var groceryList: MutableList<Table_Epicerie> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        recyclerView = findViewById(R.id.recyclerItem)
        recyclerViewCart = findViewById(R.id.recycleCart)

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

            val gridLayoutManagerItem = GridLayoutManager(this, 4)
            recyclerView.layoutManager = gridLayoutManagerItem
            val gridLayoutManagerPanier = GridLayoutManager(this, 4)
            recyclerViewCart.layoutManager = gridLayoutManagerPanier
        } else {
            val gridLayoutManagerItem = GridLayoutManager(this, 2)
            recyclerView.layoutManager = gridLayoutManagerItem
            val gridLayoutManagerPanier = GridLayoutManager(this, 2)
            recyclerViewCart.layoutManager = gridLayoutManagerPanier
        }



        genericList = mutableListOf(
            Table_Epicerie(0,"Pomme", 3, Uri.Builder().scheme("android.resource").authority(packageName).appendPath(R.drawable.img.toString()).build().toString(), "fruits", "Lorem ipsum dolor sit amet, consectetur adipiscing elit," +
                    " sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut" +
                    " aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur" +
                    " sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", false, false),
            Table_Epicerie(0,"Tomate", 2,
                Uri.Builder().scheme("android.resource").authority(packageName).appendPath(R.drawable.img_1.toString()).build().toString(), "legumes", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                    "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex" +
                    " ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat" +
                    " non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", false, false),
            Table_Epicerie(0,"Tomate Special", 1,
                Uri.Builder().scheme("android.resource").authority(packageName).appendPath(R.drawable.img_1.toString()).build().toString(), "legumes", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed" +
                    " do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo" +
                    " consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident," +
                    " sunt in culpa qui officia deserunt mollit anim id est laborum.", false, false)

        )

        val database = Database_Epicerie.getDatabase(applicationContext)
        // https://stackoverflow.com/questions/3386667/query-if-android-database-exists
        lifecycleScope.launch(Dispatchers.IO) {
            if (!(applicationContext.getDatabasePath("epicerie_database")).exists()) {
                for (epicerie in genericList) {
                    val existingItem = database.epicerieDao().findByName(epicerie.nameProduct)

                    if (existingItem == null) {
                        database.epicerieDao().insertEpicerie(epicerie)
                    }
                }
            }
            groceryList = database.epicerieDao().getAllProduct()
            cartItems = database.epicerieDao().getAllPanier()
            launch(Dispatchers.Main) {
                refreshRecyclerView()
            }
        }
        applicationContext.deleteDatabase("epicerie_database")
    }

    private fun refreshRecyclerView(){
        recyclerView.adapter = ItemAdaptor(applicationContext, this@PageListe, groceryList)
        recyclerViewCart.adapter = PanierAdaptor(applicationContext, this@PageListe, cartItems)
        recyclerView.adapter?.notifyDataSetChanged()
        recyclerViewCart.adapter?.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.page_liste_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.itemAjout -> {
                val intent = Intent(this, PageAjouter::class.java)
                startActivity(intent)
                true
            }
            R.id.HomeButton -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun ajoutPanier(item: Table_Epicerie) {
        val database = Database_Epicerie.getDatabase(applicationContext)
        lifecycleScope.launch(Dispatchers.IO) {
            // TODO MUST CHANGE CHATGPT
            val existingItem = database.epicerieDao().findByName(item.nameProduct)

            if (existingItem != null) {
                val itemPanier = existingItem.copy(isCart = true, isFavorite = false)
                database.epicerieDao().updateEpicerie(itemPanier)

                withContext(Dispatchers.Main) {
                    cartItems = database.epicerieDao().getAllPanier()
                    groceryList = database.epicerieDao().getAllProduct()
                    refreshRecyclerView()
                }
            }
        }
    }

    fun deleteProduct(item: Table_Epicerie){
        val position = groceryList.indexOf(item)
        if (position != -1) {
            groceryList.removeAt(position)
            recyclerView.adapter?.notifyItemRemoved(position)
        }
    }

    fun removeFromPanier(item: Table_Epicerie) {
        val database = Database_Epicerie.getDatabase(applicationContext)
        lifecycleScope.launch(Dispatchers.IO) {

            val itemProduct = Table_Epicerie(
                uid = item.uid,
                nameProduct = item.nameProduct,
                quantity = item.quantity,
                foodImageURI = item.foodImageURI,
                category = item.category,
                description = item.description,
                isCart = false,
                isFavorite = false
            )
            database.epicerieDao().updateEpicerie(itemProduct)
            withContext(Dispatchers.Main){
                groceryList = database.epicerieDao().getAllProduct()
                cartItems = database.epicerieDao().getAllPanier()
                refreshRecyclerView()
            }
        }
    }

    override fun onResume(){
        super.onResume()

        val database = Database_Epicerie.getDatabase(applicationContext)
        lifecycleScope.launch(Dispatchers.IO) {
            groceryList = database.epicerieDao().getAllProduct()
            cartItems = database.epicerieDao().getAllPanier()
            launch(Dispatchers.Main) {
                refreshRecyclerView()
            }
        }
    }

    fun addItemToFavorite(item: Table_Epicerie){
        val database = Database_Epicerie.getDatabase(applicationContext)
        lifecycleScope.launch(Dispatchers.IO) {
            //val existingItem = database.epicerieDao().findByName(item.nameProduct)

            val itemProduct = Table_Epicerie(
                uid = item.uid,
                nameProduct = item.nameProduct,
                quantity = item.quantity,
                foodImageURI = item.foodImageURI,
                category = item.category,
                description = item.description,
                isCart = item.isCart,
                isFavorite = true
            )

            database.epicerieDao().updateEpicerie(itemProduct)

        }

    }

    fun removeItemFromFavorite(item: Table_Epicerie){
        val database = Database_Epicerie.getDatabase(applicationContext)

        lifecycleScope.launch(Dispatchers.IO) {

            val itemProduct = Table_Epicerie(
                uid = item.uid,
                nameProduct = item.nameProduct,
                quantity = item.quantity,
                foodImageURI = item.foodImageURI,
                category = item.category,
                description = item.description,
                isCart = item.isCart,
                isFavorite = false
            )
            database.epicerieDao().updateEpicerie(itemProduct)
        }
    }
}