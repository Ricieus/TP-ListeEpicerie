package com.example.tp_listeepicerie

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
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
import com.example.tp_listeepicerie.recyclerCart.CartAdaptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PageList : AppCompatActivity() {
    private var cartItems: MutableList<Table_Grocery> = mutableListOf()
    private lateinit var recyclerViewCart: RecyclerView
    private lateinit var recyclerView: RecyclerView
    private var groceryList: MutableList<Table_Grocery> = mutableListOf()

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


        val database = Database_Epicerie.getDatabase(applicationContext)
        lifecycleScope.launch(Dispatchers.IO) {
            groceryList = database.GroceryDAO().getAllProduct()
            cartItems = database.GroceryDAO().getAllPanier()
            launch(Dispatchers.Main) {
                refreshRecyclerView()
            }
        }
    }

    fun refreshRecyclerView() {
        recyclerView.adapter = ItemAdaptor(applicationContext, this@PageList, groceryList)
        recyclerViewCart.adapter = CartAdaptor(applicationContext, this@PageList, cartItems)
        recyclerView.adapter?.notifyDataSetChanged()
        recyclerViewCart.adapter?.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.page_list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.itemAjout -> {
                val intent = Intent(this, PageAddItem::class.java)
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

    fun ajoutPanier(item: Table_Grocery) {
        val database = Database_Epicerie.getDatabase(applicationContext)
        lifecycleScope.launch(Dispatchers.IO) {
            val existingItem = database.GroceryDAO().findByName(item.nameProduct)

            if (existingItem != null) {
                val itemPanier = Table_Grocery(
                    uid = item.uid,
                    nameProduct = item.nameProduct,
                    quantity = item.quantity,
                    foodImageURI = item.foodImageURI,
                    category = item.category,
                    description = item.description,
                    isCart = true,
                    isFavorite = item.isFavorite
                )
                database.GroceryDAO().updateEpicerie(itemPanier)

                withContext(Dispatchers.Main) {
                    cartItems = database.GroceryDAO().getAllPanier()
                    groceryList = database.GroceryDAO().getAllProduct()
                    refreshRecyclerView()
                }
            }
        }
    }

    fun removeFromPanier(item: Table_Grocery) {
        val database = Database_Epicerie.getDatabase(applicationContext)
        lifecycleScope.launch(Dispatchers.IO) {

            val itemProduct = Table_Grocery(
                uid = item.uid,
                nameProduct = item.nameProduct,
                quantity = item.quantity,
                foodImageURI = item.foodImageURI,
                category = item.category,
                description = item.description,
                isCart = false,
                isFavorite = item.isFavorite
            )
            database.GroceryDAO().updateEpicerie(itemProduct)
            withContext(Dispatchers.Main) {
                groceryList = database.GroceryDAO().getAllProduct()
                cartItems = database.GroceryDAO().getAllPanier()
                refreshRecyclerView()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val database = Database_Epicerie.getDatabase(applicationContext)
        lifecycleScope.launch(Dispatchers.IO) {
            groceryList = database.GroceryDAO().getAllProduct()
            cartItems = database.GroceryDAO().getAllPanier()
            launch(Dispatchers.Main) {
                refreshRecyclerView()
            }
        }
    }

    fun addItemToFavorite(item: Table_Grocery) {
        val database = Database_Epicerie.getDatabase(applicationContext)
        lifecycleScope.launch(Dispatchers.IO) {

            val itemProduct = Table_Grocery(
                uid = item.uid,
                nameProduct = item.nameProduct,
                quantity = item.quantity,
                foodImageURI = item.foodImageURI,
                category = item.category,
                description = item.description,
                isCart = item.isCart,
                isFavorite = true
            )
            database.GroceryDAO().updateEpicerie(itemProduct)
        }
    }

    fun removeItemFromFavorite(item: Table_Grocery) {
        val database = Database_Epicerie.getDatabase(applicationContext)

        lifecycleScope.launch(Dispatchers.IO) {

            val itemProduct = Table_Grocery(
                uid = item.uid,
                nameProduct = item.nameProduct,
                quantity = item.quantity,
                foodImageURI = item.foodImageURI,
                category = item.category,
                description = item.description,
                isCart = item.isCart,
                isFavorite = false
            )
            database.GroceryDAO().updateEpicerie(itemProduct)
        }
    }
}