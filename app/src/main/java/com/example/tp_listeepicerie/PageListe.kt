package com.example.tp_listeepicerie

import android.content.Intent
import android.net.Uri
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
import com.example.tp_listeepicerie.recyclerPanier.PanierAdaptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PageListe : AppCompatActivity(){
    private var genericList: MutableList<Table_Epicerie> = mutableListOf()
    private var cartItems: MutableList<Table_Panier> = mutableListOf()
    private lateinit var recyclerViewCart: RecyclerView
    private lateinit var recyclerView: RecyclerView
    private var listEpicerie: MutableList<Table_Epicerie> = mutableListOf()

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

        val gridLayoutManagerItem = GridLayoutManager(this, 2)
        recyclerView.layoutManager = gridLayoutManagerItem
        val gridLayoutManagerPanier = GridLayoutManager(this, 2)
        recyclerViewCart.layoutManager = gridLayoutManagerPanier



        genericList = mutableListOf(
            Table_Epicerie(0,"Pomme", 2.50, 1, Uri.Builder().scheme("android.resource").authority(packageName).appendPath(R.drawable.img.toString()).build().toString(), "fruits", "Lorem ipsum dolor sit amet, consectetur adipiscing elit," +
                    " sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut" +
                    " aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur" +
                    " sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", R.id.btnAjout, R.id.btnAjout),
            Table_Epicerie(0,"Tomate", 3.25, 1,
                Uri.Builder().scheme("android.resource").authority(packageName).appendPath(R.drawable.img_1.toString()).build().toString(), "legumes", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                    "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex" +
                    " ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat" +
                    " non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",R.id.btnAjout, R.id.btnAjout),
            Table_Epicerie(0,"Tomate Special", 5.00, 1,
                Uri.Builder().scheme("android.resource").authority(packageName).appendPath(R.drawable.img_1.toString()).build().toString(), "legumes", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed" +
                    " do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo" +
                    " consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident," +
                    " sunt in culpa qui officia deserunt mollit anim id est laborum.",R.id.btnAjout, R.id.btnAjout)

        )

        val database = Database_Epicerie.getDatabase(applicationContext)
        // https://stackoverflow.com/questions/3386667/query-if-android-database-exists
        lifecycleScope.launch(Dispatchers.IO) {
            if (!(applicationContext.getDatabasePath("epicerie_database")).exists()) {
                for (epicerie in genericList) {
                    val existingItem = database.epicerieDao().findByName(epicerie.nom)

                    if (existingItem == null) {
                        database.epicerieDao().insertEpicerie(epicerie)
                    }
                }
            }



            listEpicerie = database.epicerieDao().getAll()
            cartItems = database.epicerieDao().getAllPanier()
            launch(Dispatchers.Main) {
                recyclerView.adapter = ItemAdaptor(applicationContext, this@PageListe, listEpicerie)
                recyclerViewCart.adapter = PanierAdaptor(applicationContext, this@PageListe, cartItems)
            }
        }

//        val dbName = "epicerie_database"
//        applicationContext.deleteDatabase(dbName)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
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
            for (epicerie in database.epicerieDao().getAll()) {
                val existingItem = database.epicerieDao().findByName(item.nom)

                if (existingItem?.nom == epicerie.nom) {
                    database.epicerieDao().deleteEpicerie(existingItem)

                    val itemPanier = Table_Panier(
                        uid = epicerie.uid,
                        nom = epicerie.nom,
                        prix = epicerie.prix,
                        quantite = epicerie.quantite,
                        imageNourriture = epicerie.imageNourriture,
                        categorie = epicerie.categorie,
                        description = epicerie.description,
                        boutonPanier = epicerie.boutonPanier,
                        boutonInformation = epicerie.boutonInformation
                    )
                    database.epicerieDao().insertPanier(itemPanier)

                    withContext(Dispatchers.Main){
                        cartItems.add(itemPanier)
                        recyclerViewCart.adapter?.notifyItemInserted(cartItems.size - 1)
                        deleteProduct(item)
                    }
                }
            }

        }
    }

    fun deleteProduct(item: Table_Epicerie){
        val position = listEpicerie.indexOf(item)
        if (position != -1) {
            listEpicerie.removeAt(position)
            recyclerView.adapter?.notifyItemRemoved(position)
        }

    }

    fun removeFromPanier(item: Table_Panier) {
        val database = Database_Epicerie.getDatabase(applicationContext)
        lifecycleScope.launch(Dispatchers.IO) {
            database.epicerieDao().deleteEpiceriePanier(item)

            val itemProduct = Table_Epicerie(
                uid = item.uid,
                nom = item.nom,
                prix = item.prix,
                quantite = item.quantite,
                imageNourriture = item.imageNourriture,
                categorie = item.categorie,
                description = item.description,
                boutonPanier = item.boutonPanier,
                boutonInformation = item.boutonInformation
            )

            database.epicerieDao().insertProductList(itemProduct)

            withContext(Dispatchers.Main) {
                listEpicerie.add(itemProduct)
                recyclerView.adapter?.notifyItemInserted(genericList.size - 1)

                val position = cartItems.indexOf(item)
                if (position != -1) {
                    cartItems.removeAt(position)
                    recyclerViewCart.adapter?.notifyItemRemoved(position)
                }
            }
        }
    }

    override fun onResume(){
        super.onResume()

        val database = Database_Epicerie.getDatabase(applicationContext)
        lifecycleScope.launch(Dispatchers.IO) {
            listEpicerie = database.epicerieDao().getAll()
            cartItems = database.epicerieDao().getAllPanier()
            launch(Dispatchers.Main) {
                recyclerView.adapter = ItemAdaptor(applicationContext, this@PageListe, listEpicerie)
                recyclerViewCart.adapter = PanierAdaptor(applicationContext, this@PageListe, cartItems)
            }
        }
    }
}