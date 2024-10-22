package com.example.tp_listeepicerie

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PageAjouter : AppCompatActivity() {

    private lateinit var btnAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_page_ajouter)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val nameItem: EditText = findViewById(R.id.NameEdit)
        val quantityItem: EditText = findViewById(R.id.QuantityEdit)
        val categoryItem: EditText = findViewById(R.id.CategoryEdit)
        val descriptionItem: EditText = findViewById(R.id.DescriptionEdit)


        btnAdd = findViewById(R.id.btnSave)
        btnAdd.setOnClickListener {
            val database = Database_Epicerie.getDatabase(applicationContext)
            lifecycleScope.launch(Dispatchers.IO) {


                val itemEpicerie = Table_Epicerie(
                    uid = 0,
                    nom = nameItem.text.toString(),
                    prix = 0.0,
                    quantite = quantityItem.text.toString().toIntOrNull() ?: 0, //Source chatgpt
                    imageNourriture = R.drawable.img,
                    categorie = categoryItem.text.toString(),
                    description = descriptionItem.text.toString(),
                    boutonPanier = 2131230818,
                    boutonInformation = 2131230818
                )
                database.epicerieDao().insertEpicerie(itemEpicerie)

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.page_details_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.retourner -> {
                //Temporaire
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}