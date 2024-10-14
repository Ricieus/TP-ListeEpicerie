package com.example.tp_listeepicerie

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.recycler.GenericItemAdaptor
import com.example.tp_listeepicerie.recycler.GenericItemHolder



class MainActivity : AppCompatActivity() {
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

        val recyclerView: RecyclerView = findViewById(R.id.recycler)

        val gridLayoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = gridLayoutManager

        val genericList = listOf(
            GenericItem("Pomme", 2, R.drawable.img, "fruits", R.id.btnAjout, R.id.btnAjout),
            GenericItem("Tomate", 3, R.drawable.img_1, "legumes", R.id.btnAjout, R.id.btnAjout),
            GenericItem("Tomate Special", 5, R.drawable.img_1, "legumes", R.id.btnAjout, R.id.btnAjout),
            GenericItem("Pomme", 2, R.drawable.img, "fruits", R.id.btnAjout, R.id.btnAjout),
            GenericItem("Tomate", 3, R.drawable.img_1, "legumes", R.id.btnAjout, R.id.btnAjout),
            GenericItem("Tomate Special", 5, R.drawable.img_1, "legumes", R.id.btnAjout, R.id.btnAjout),
        )
        recyclerView.adapter = GenericItemAdaptor(applicationContext, this, genericList)
        
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return super.onCreateOptionsMenu(menu)}

}

data class GenericItem(var nom: String, var prix: Int, var imageNourriture: Int, var categorie: String, var boutonPanier: Int, var boutonInformation: Int)