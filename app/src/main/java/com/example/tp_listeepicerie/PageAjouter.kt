package com.example.tp_listeepicerie

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PageAjouter : AppCompatActivity() {

    private lateinit var btnAdd: Button
    private lateinit var btnPhotoImg: Button
    private lateinit var btnLoadImg: Button
    private lateinit var productImage: ImageView

    private var imageUri: Uri? = null

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

        val selectionPhoto =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uriSelect: Uri? ->
                if (uriSelect != null) {
                    applicationContext.contentResolver.takePersistableUriPermission(
                        uriSelect,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    productImage.setImageURI(uriSelect)
                    imageUri = uriSelect
                }
            }


        val uriPhoto = creerUriPhoto()
        val prendrePhoto =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    productImage.setImageURI(uriPhoto)
                    imageUri = uriPhoto
                }
            }

        btnPhotoImg = findViewById(R.id.btnPhotoImg)
        btnLoadImg = findViewById(R.id.btnLoadImg)
        btnAdd = findViewById(R.id.btnSave)

        btnAdd.setOnClickListener {
            val database = Database_Epicerie.getDatabase(applicationContext)
            lifecycleScope.launch(Dispatchers.IO) {
                if (imageUri != null) {
                    val itemEpicerie = Table_Epicerie(
                        uid = 0,
                        nameProduct = nameItem.text.toString(),
                        price = 0.0,
                        quantity = quantityItem.text.toString().toIntOrNull() ?: 1,
                        FoodImageURI = imageUri.toString(),
                        category = categoryItem.text.toString(),
                        description = descriptionItem.text.toString(),
                        boutonPanier = 2131230818,
                        boutonInformation = 2131230818
                    )
                    database.epicerieDao().insertEpicerie(itemEpicerie)
                }
            }
        }

        btnPhotoImg.setOnClickListener {
            prendrePhoto.launch(uriPhoto)
        }
        btnLoadImg.setOnClickListener {
            selectionPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun creerUriPhoto(): Uri {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val photoFile: File =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "IMG_$timeStamp.jpg")
        return FileProvider.getUriForFile(this, "ca.qc.bdeb.c5gm.photoapp", photoFile)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.page_ajouter_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.retourner -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
