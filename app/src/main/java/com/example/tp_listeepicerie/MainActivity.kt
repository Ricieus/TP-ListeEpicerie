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
    private val recyclerViewCart: RecyclerView
        get() {
            TODO()
        }

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

        val recyclerView: RecyclerView = findViewById(R.id.recyclerItem)
        val recyclerViewCart: RecyclerView = findViewById(R.id.recycleCart)

        val gridLayoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = gridLayoutManager

        val genericList = listOf(
            GenericItem("Pomme", 2.50, R.drawable.img, "fruits", R.id.btnAjout, R.id.btnAjout),
            GenericItem("Tomate", 3.25, R.drawable.img_1, "legumes", R.id.btnAjout, R.id.btnAjout),
            GenericItem("Tomate Special", 5.00, R.drawable.img_1, "legumes", R.id.btnAjout, R.id.btnAjout),
            GenericItem("Pomme", 2.50, R.drawable.img, "fruits", R.id.btnAjout, R.id.btnAjout),
            GenericItem("Tomate", 3.25, R.drawable.img_1, "legumes", R.id.btnAjout, R.id.btnAjout),
            GenericItem("Tomate Special", 5.00, R.drawable.img_1, "legumes", R.id.btnAjout, R.id.btnAjout),
        )
        recyclerView.adapter = ItemAdaptor(applicationContext, this, genericList)
        recyclerViewCart.adapter = PanierAdaptor(applicationContext, this, cartItems)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun addToCart(item: GenericItem) {
        cartItems.add(item)
        recyclerViewCart.adapter?.notifyItemInserted(cartItems.size - 1)
    }

}

data class GenericItem(var nom: String, var prix: Double, var imageNourriture: Int, var categorie: String, var boutonPanier: Int, var boutonInformation: Int)