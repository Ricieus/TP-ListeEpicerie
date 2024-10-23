package com.example.tp_listeepicerie

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PageAjouter : AppCompatActivity() {

    private lateinit var btnAdd: Button
    private lateinit var btnLoadImg: Button
    private lateinit var productImage: ImageView


    private var imageUri: Uri? = null
    private val selectionPhoto =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) productImage.setImageURI(uri)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_page_ajouter)
        productImage = findViewById(R.id.imageLoad)
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




        btnLoadImg = findViewById(R.id.btnLoadImg)
        btnAdd = findViewById(R.id.btnSave)
        btnAdd.setOnClickListener {
            val database = Database_Epicerie.getDatabase(applicationContext)
            lifecycleScope.launch(Dispatchers.IO) {

                val itemEpicerie = Table_Epicerie(
                    uid = 0,
                    nom = nameItem.text.toString(),
                    prix = 0.0,
                    quantite = quantityItem.text.toString().toIntOrNull() ?: 0, //Source chatgpt
                    imageNourriture = imageUri.toString(),
                    categorie = categoryItem.text.toString(),
                    description = descriptionItem.text.toString(),
                    boutonPanier = 2131230818,
                    boutonInformation = 2131230818
                )
                database.epicerieDao().insertEpicerie(itemEpicerie)
            }
        }


        btnLoadImg.setOnClickListener {
            selectionPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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