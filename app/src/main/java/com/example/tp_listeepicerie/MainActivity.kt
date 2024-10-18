package com.example.tp_listeepicerie

import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.recyclerItem.ItemAdaptor
import com.example.tp_listeepicerie.recyclerPanier.PanierAdaptor

class MainActivity : AppCompatActivity() {

    private var genericList: MutableList<GenericItem> = mutableListOf()
    private var cartItems: MutableList<GenericItem> = mutableListOf()
    private lateinit var recyclerViewCart: RecyclerView
    private lateinit var recyclerView: RecyclerView

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

        val genericList = listOf(
            GenericItem("Pomme", 2.50, 1, R.drawable.img, "fruits", "Lorem ipsum dolor sit amet, consectetur adipiscing elit," +
                    " sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut" +
                    " aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur" +
                    " sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", R.id.btnAjout, R.id.btnAjout),
            GenericItem("Tomate", 3.25, 1,R.drawable.img_1, "legumes", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                    "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex" +
                    " ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat" +
                    " non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",R.id.btnAjout, R.id.btnAjout),
            GenericItem("Tomate Special", 5.00, 1,R.drawable.img_1, "legumes", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed" +
                    " do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo" +
                    " consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident," +
                    " sunt in culpa qui officia deserunt mollit anim id est laborum.",R.id.btnAjout, R.id.btnAjout),
            GenericItem("Pomme", 2.50, 1,R.drawable.img, "fruits", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor" +
                    " incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure" +
                    " dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt" +
                    " mollit anim id est laborum.",R.id.btnAjout, R.id.btnAjout),
            GenericItem("Tomate", 3.25, 1,R.drawable.img_1, "legumes", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor" +
                    " incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor" +
                    " in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim" +
                    " id est laborum.",R.id.btnAjout, R.id.btnAjout),
            GenericItem("Tomate Special", 5.00, 1,R.drawable.img_1, "legumes", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor " +
                    "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in " +
                    "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",R.id.btnAjout, R.id.btnAjout),
        )
        recyclerView.adapter = ItemAdaptor(applicationContext, this, genericList)
        recyclerViewCart.adapter = PanierAdaptor(applicationContext, this, cartItems)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun ajoutPanier(item: GenericItem) {
        cartItems.add(item)
        recyclerViewCart.adapter?.notifyItemInserted(cartItems.size - 1)

    }

//    fun deleteProduct(item: GenericItem){
//        val index = genericList.indexOf(item)
//
//            genericList.removeAt(index)
//            recyclerView.adapter?.notifyItemRemoved(index)
//    }
}

data class GenericItem(
    var nom: String,
    var prix: Double,
    var quantite: Int,
    var imageNourriture: Int,
    var categorie: String,
    var description: String,
    var boutonPanier: Int,
    var boutonInformation: Int
)
