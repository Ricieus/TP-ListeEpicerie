package com.example.tp_listeepicerie

import android.content.Intent
import android.hardware.usb.UsbManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
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
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.tp_listeepicerie.recyclerItem.InfoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PageDetails : AppCompatActivity() {

    private lateinit var textProductName: TextView
    private lateinit var productImage: ImageView
    private lateinit var textProductDescription: TextView
    //private lateinit var editButton: ImageButton
    private lateinit var textCategory: TextView
    private lateinit var textQuantity: TextView
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private lateinit var updateImageButton: Button
    private lateinit var takePhotoButton: Button

    private var imageUri: Uri? = null


    private var productId: Int = 0
    private var itemName: String = ""
    private var itemImage: String = ""
    private var productDescription: String = ""
    private var itemCategory: String = ""
    private var itemQuantity: Int = 0

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

        val infoItem: InfoItem
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) { // TIRAMISU onwards
            infoItem = intent.getParcelableExtra("InfoItem", InfoItem::class.java)!!
        } else {
            infoItem = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)!!
        }

        infoItem.let {
            productId = infoItem.uid
            itemName = infoItem.nameItem
            itemImage = infoItem.FoodImageURI
            productDescription = infoItem.description
            itemCategory = infoItem.category
            itemQuantity = infoItem.quantity
        }

        textProductName = findViewById(R.id.productName)
        productImage = findViewById(R.id.imageProduit)
        textProductDescription = findViewById(R.id.productDescription)
        //editButton = findViewById(R.id.editButton)
        textCategory = findViewById(R.id.productCategory)
        textQuantity = findViewById(R.id.productQuantity)
        saveButton = findViewById(R.id.saveButton)
        deleteButton = findViewById(R.id.deleteItem)
        updateImageButton = findViewById(R.id.changeImageButton)
        takePhotoButton = findViewById(R.id.takePhotoButton)

        if (itemImage.isNotEmpty()){
            val imageUri = Uri.parse(itemImage)
            productImage.setImageURI(imageUri)
            this.imageUri = imageUri
        }

        val selectionPhoto = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uriSelect: Uri? ->
            if (uriSelect != null) {
                applicationContext.contentResolver.takePersistableUriPermission(uriSelect, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                productImage.setImageURI(uriSelect)
                imageUri = uriSelect
            }
        }


        val uriPhoto = creerUriPhoto()
        val prendrePhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                productImage.setImageURI(uriPhoto)
                imageUri = uriPhoto
            }
        }

        textProductName.text = itemName
        textProductDescription.text = productDescription
        textCategory.text = itemCategory
        textQuantity.text = itemQuantity.toString()

        deleteButton.setOnClickListener {
            deleteItem()
        }

        updateImageButton.setOnClickListener {
            selectionPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        takePhotoButton.setOnClickListener {
            prendrePhoto.launch(uriPhoto)
        }


        saveButton.setOnClickListener {
            updateItems()
            finish()
        }


    }
    private fun creerUriPhoto(): Uri {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val photoFile: File = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "IMG_$timeStamp.jpg")
        return FileProvider.getUriForFile(this, "ca.qc.bdeb.c5gm.photoapp", photoFile)
    }

    private fun updateItems() {
        val updatedName = textProductName.text.toString()
        val updatedDescription = textProductDescription.text.toString()
        val updatedCategory = textCategory.text.toString()
        val updatedQuantity = textQuantity.text.toString().toInt()
        val updatedImageUri = imageUri.toString()
        val database = Database_Epicerie.getDatabase(applicationContext)

        lifecycleScope.launch(Dispatchers.IO) {
            val updatedItem = Table_Epicerie(
                uid = productId,
                nameProduct = updatedName,
                description = updatedDescription,
                price = 0.0,
                category = updatedCategory,
                quantity = updatedQuantity,
                FoodImageURI = updatedImageUri,
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
            R.id.edit -> {
                updateImageButton.isEnabled = true
                takePhotoButton.isEnabled = true
                textProductName.isEnabled = true
                textProductDescription.isEnabled = true
                textCategory.isEnabled = true
                textQuantity.isEnabled = true
                saveButton.isEnabled = true
            }

        }
        return super.onOptionsItemSelected(item)
    }
}