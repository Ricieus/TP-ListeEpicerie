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
        val genericList = listOf(
            GenericItem("name1", 1, R.drawable.img, "fruits", R.id.buttonAcheter),
            GenericItem("name2", 1, R.drawable.img_1, "legumes", R.id.buttonAcheter),
            GenericItem("name3", 1, R.drawable.img_1, "legumes", R.id.buttonAcheter),
        )
        recyclerView.adapter = GenericItemAdaptor(applicationContext, this, genericList)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return super.onCreateOptionsMenu(menu)}

}

data class GenericItem(var nom: String, var quantite: Int, var image: Int, var categorie: String, var bouton: Int)