package com.example.tp_listeepicerie.page

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import androidx.core.content.FileProvider
import androidx.core.text.isDigitsOnly
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.tp_listeepicerie.Database_Epicerie
import com.example.tp_listeepicerie.R
import com.example.tp_listeepicerie.Table_Grocery
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PageAddItem : AppCompatActivity() {

    private lateinit var btnAdd: Button
    private lateinit var btnPhotoImg: Button
    private lateinit var btnLoadImg: Button
    private lateinit var productImage: ImageView

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_page_add_item)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        initializeVariables()

        //Permet de récupérer l'URI de la photo
        val uriPhoto = createUriPhoto()

        val takePhoto = takePhotoFromCamera(uriPhoto)
        val photoSelection = importImageFromDevice()

        btnAdd.setOnClickListener {
            addNewProduct()
        }

        btnPhotoImg.setOnClickListener {
            takePhoto.launch(uriPhoto)
        }

        btnLoadImg.setOnClickListener {
            photoSelection.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }


    private fun addNewProduct() {
        val nameItem: EditText = findViewById(R.id.NameEdit)
        val quantityItem: EditText = findViewById(R.id.QuantityEdit)
        val categoryItem: EditText = findViewById(R.id.CategoryEdit)
        val descriptionItem: EditText = findViewById(R.id.DescriptionEdit)

        //Permet de gérer les nulles/vides (Inspiré de ChatGPT)
        if (nameItem.text.isNullOrBlank() || quantityItem.text.isNullOrBlank() || categoryItem.text.isNullOrBlank() || descriptionItem.text.isNullOrBlank() || imageUri == null) {
            Snackbar.make(
                findViewById(R.id.main),
                "Veuillez remplir tous les informations nécessaires", Snackbar.LENGTH_LONG)
                .show()

        } else if(!quantityItem.text.isDigitsOnly() || quantityItem.text.toString().toInt() <= 0){
            Snackbar.make(findViewById(R.id.main), "Veuillez entrer une quantité valide", Snackbar.LENGTH_LONG).show()
        } else{
            val database = Database_Epicerie.getDatabase(applicationContext)
            lifecycleScope.launch(Dispatchers.IO) {
                if (imageUri != null) {
                    val itemGrocery = Table_Grocery(
                        uid = 0,
                        nameProduct = nameItem.text.toString(),
                        quantity = quantityItem.text.toString().toIntOrNull() ?: 1,
                        foodImageURI = imageUri.toString(),
                        category = categoryItem.text.toString(),
                        description = descriptionItem.text.toString(),
                        isCart = false,
                        isFavorite = false
                    )
                    database.GroceryDAO().insertEpicerie(itemGrocery)
                    finish()
                }
            }
        }
    }

    private fun takePhotoFromCamera(photoUri: Uri) =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                productImage.setImageURI(photoUri)
                imageUri = photoUri
            }
        }


    private fun importImageFromDevice() =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uriSelect: Uri? ->
            if (uriSelect != null) {
                applicationContext.contentResolver.takePersistableUriPermission(
                    uriSelect, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                productImage.setImageURI(uriSelect)
                imageUri = uriSelect
            }
        }


    private fun initializeVariables() {
        productImage = findViewById(R.id.imageLoad)
        btnPhotoImg = findViewById(R.id.btnPhotoImg)
        btnLoadImg = findViewById(R.id.btnLoadImg)
        btnAdd = findViewById(R.id.btnSave)
    }

    private fun createUriPhoto(): Uri {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val photoFile: File =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "IMG_$timeStamp.jpg")
        return FileProvider.getUriForFile(this, "ca.qc.bdeb.c5gm.photoapp", photoFile)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.page_add_item_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.back -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
