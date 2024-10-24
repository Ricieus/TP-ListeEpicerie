package com.example.tp_listeepicerie

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import kotlinx.coroutines.withContext

class PageDetails : AppCompatActivity() {

    private lateinit var textProductName: TextView
    private lateinit var productImage: ImageView
    private lateinit var textProductDescription: TextView
    private lateinit var editButton: ImageButton
    private lateinit var textCategory: TextView
    private lateinit var textQuantity: TextView
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private var productId: Int = 0

    private lateinit var updateImageButton: Button

    private var imageUri: Uri? = null
    private val selectionPhoto =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) productImage.setImageURI(uri)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_page_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        productId = intent.getIntExtra("productId", -1)
        val itemName = intent.getStringExtra("nomProduit")
        val itemImage = intent.getStringExtra("imageProduit")
        val productDescription = intent.getStringExtra("productDescription")
        val itemCategory = intent.getStringExtra("productCategory")
        val itemQuantity = intent.getIntExtra("productQuantity", 1)

        textProductName = findViewById(R.id.productName)
        productImage = findViewById(R.id.imageProduit)
        textProductDescription = findViewById(R.id.productDescription)
        editButton = findViewById(R.id.editButton)
        textCategory = findViewById(R.id.productCategory)
        textQuantity = findViewById(R.id.productQuantity)
        saveButton = findViewById(R.id.saveButton)
        deleteButton = findViewById(R.id.deleteItem)
        updateImageButton = findViewById(R.id.changeImageButton)

        if (imageUri != null) {
            productImage.setImageURI(Uri.parse(itemImage))
        } else {
            productImage.setImageResource(R.drawable.img)
        }

        textProductName.text = itemName
        textProductDescription.text = productDescription
        textCategory.text = itemCategory
        textQuantity.text = itemQuantity.toString()

        editButton.setOnClickListener {
            textProductName.isEnabled = true
            textProductDescription.isEnabled = true
            textCategory.isEnabled = true
            textQuantity.isEnabled = true
            saveButton.isEnabled = true
        }

        deleteButton.setOnClickListener {
            deleteItem()
        }

        updateImageButton.setOnClickListener {
            selectionPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        saveButton.setOnClickListener {
            updateItems()
        }


    }

    private fun updateItems() {
        val updatedName = textProductName.text.toString()
        val updatedDescription = textProductDescription.text.toString()
        val updatedCategory = textCategory.text.toString()
        val updatedQuantity = textQuantity.text.toString().toInt()

        val database = Database_Epicerie.getDatabase(applicationContext)

        lifecycleScope.launch(Dispatchers.IO) {
            val updatedItem = Table_Epicerie(
                uid = productId,
                nom = updatedName,
                description = updatedDescription,
                prix = 0.0,
                categorie = updatedCategory,
                quantite = updatedQuantity,
                imageNourriture = imageUri.toString(),
                boutonInformation = 2131230818,
                boutonPanier = 2131230818
            )
            database.epicerieDao().updateEpicerie(updatedItem)

            withContext(Dispatchers.Main) {
                Toast.makeText(
                    applicationContext,
                    "Le produit à était mis à jours",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun deleteItem() {
        val database = Database_Epicerie.getDatabase(applicationContext)

        lifecycleScope.launch(Dispatchers.IO) {
            val itemDelete = database.epicerieDao().getEpicerieId(productId)
            if (itemDelete != null) {
                database.epicerieDao().deleteEpicerie(itemDelete)
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(
                    applicationContext,
                    "Le produit à était supprimer avec succès",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
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