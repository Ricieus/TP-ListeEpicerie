package com.example.tp_listeepicerie

import android.content.Intent
import android.hardware.usb.UsbManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
            infoItem = intent.getParcelableExtra("InfoItem", InfoItem::class.java)!!
        } else {
            infoItem = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)!!
        }

        productId = infoItem.uid
        itemName = infoItem.nameItem
        itemImage = infoItem.FoodImageURI
        productDescription = infoItem.description
        itemCategory = infoItem.category
        itemQuantity = infoItem.quantity

        initializeVariables()
        loadImageOfProducts()


        val selectionPhoto = takeImageFromDevice()

        //Permet de récupérer l'URI de la photo
        val uriPhoto = createUriPhoto()

        val takePhoto = photoFromCamera(uriPhoto)

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
            takePhoto.launch(uriPhoto)
        }

        saveButton.setOnClickListener {
            updateItems()
        }
    }

    private fun takeImageFromDevice() =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uriSelect: Uri? ->
            if (uriSelect != null) {
                applicationContext.contentResolver.takePersistableUriPermission(
                    uriSelect, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                productImage.setImageURI(uriSelect)
                imageUri = uriSelect
            }
        }

    private fun photoFromCamera(photoUri: Uri) =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                productImage.setImageURI(photoUri)
                imageUri = photoUri
            }
        }

    private fun loadImageOfProducts() {
        if (itemImage.isNotEmpty()) {
            val imageUri = Uri.parse(itemImage)
            productImage.setImageURI(imageUri)
            this.imageUri = imageUri //Aidé par ChatGPT
        }
    }

    private fun initializeVariables() {
        textProductName = findViewById(R.id.productName)
        productImage = findViewById(R.id.imageProduit)
        textProductDescription = findViewById(R.id.productDescription)
        textCategory = findViewById(R.id.productCategory)
        textQuantity = findViewById(R.id.productQuantity)
        saveButton = findViewById(R.id.saveButton)
        deleteButton = findViewById(R.id.deleteItem)
        updateImageButton = findViewById(R.id.changeImageButton)
        takePhotoButton = findViewById(R.id.takePhotoButton)
    }

    private fun createUriPhoto(): Uri {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val photoFile: File =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "IMG_$timeStamp.jpg")
        return FileProvider.getUriForFile(this, "ca.qc.bdeb.c5gm.photoapp", photoFile)
    }

    private fun updateItems() {
        //ChatGPT m'a aidé à gérer les valeurs nulles/vides
        val updatedName: String? = textProductName.text.toString().takeIf { it.isNotBlank() }
        val updatedDescription: String? =
            textProductDescription.text.toString().takeIf { it.isNotBlank() }
        val updatedCategory: String? = textCategory.text.toString().takeIf { it.isNotBlank() }
        val updatedQuantity: Int? = textQuantity.text.toString().toIntOrNull()
        val updatedImageUri: String? = imageUri?.toString()

        //Permet de gérer les nulles/vides (Inspiré de ChatGPT)
        if (updatedName.isNullOrBlank() || updatedDescription.isNullOrBlank() || updatedCategory.isNullOrBlank() || updatedQuantity == null || updatedImageUri.isNullOrBlank()
        ) {
            Toast.makeText(
                this@PageDetails,
                "Veuillez remplir tous les informations nécessaires",
                Toast.LENGTH_LONG
            ).show()
        } else {
            lifecycleScope.launch(Dispatchers.IO) {
                val database = Database_Epicerie.getDatabase(applicationContext)
                val currentItem = database.GroceryDAO().findByName(itemName)
                if (currentItem != null) {
                    val updatedItem = Table_Grocery(
                        uid = productId,
                        nameProduct = updatedName,
                        description = updatedDescription,
                        category = updatedCategory,
                        quantity = updatedQuantity,
                        foodImageURI = updatedImageUri,
                        isCart = currentItem.isCart,
                        isFavorite = currentItem.isFavorite
                    )
                    database.GroceryDAO().insertEpicerie(updatedItem)

                    withContext(Dispatchers.Main) { //Aidé par ChatGPT
                        Toast.makeText(
                            applicationContext, "Le produit à était mis à jours", Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }
            }
        }
    }

    private fun deleteItem() {
        val database = Database_Epicerie.getDatabase(applicationContext)

        lifecycleScope.launch(Dispatchers.IO) {
            val itemDelete = database.GroceryDAO().getEpicerieId(productId)
            if (itemDelete != null) {
                database.GroceryDAO().deleteEpicerie(itemDelete)
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
            R.id.returnBack -> {
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