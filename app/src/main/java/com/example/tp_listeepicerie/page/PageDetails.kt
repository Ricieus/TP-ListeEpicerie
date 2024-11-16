package com.example.tp_listeepicerie.page

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
import com.example.tp_listeepicerie.Database_Epicerie
import com.example.tp_listeepicerie.R
import com.example.tp_listeepicerie.Table_Grocery
import com.example.tp_listeepicerie.fragment.more_detail
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

        val intent = intent
        val infoItem = intent.getParcelableExtra<InfoItem>("InfoItem")

        if (infoItem != null) {
            val fragment = more_detail().apply {
                arguments = Bundle().apply {
                    putParcelable("InfoItem", infoItem)
                }
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit()
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