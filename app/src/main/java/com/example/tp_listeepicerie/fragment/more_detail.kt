package com.example.tp_listeepicerie.fragment

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.tp_listeepicerie.Database_Epicerie
import com.example.tp_listeepicerie.R
import com.example.tp_listeepicerie.Table_Grocery
import com.example.tp_listeepicerie.recyclerItem.InfoItem
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class more_detail : Fragment() {
    private lateinit var infoItem: InfoItem
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<InfoItem>("InfoItem")?.let {
            infoItem = it
            productId = infoItem.uid
            itemName = infoItem.nameItem
            itemImage = infoItem.FoodImageURI
            productDescription = infoItem.description
            itemCategory = infoItem.category
            itemQuantity = infoItem.quantity
        }

        initializeVariables(view)
        loadImageOfProducts()

        val selectionPhoto = takeImageFromDevice()
        val uriPhoto = createUriPhoto()
        val takePhoto = photoFromCamera(uriPhoto)

        textProductName.text = itemName
        textProductDescription.text = productDescription
        textCategory.text = itemCategory
        textQuantity.text = itemQuantity.toString()

        deleteButton.setOnClickListener { deleteItem() }
        updateImageButton.setOnClickListener {
            selectionPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        takePhotoButton.setOnClickListener { takePhoto.launch(uriPhoto) }
        saveButton.setOnClickListener { updateItems() }
    }

    fun enableModification(isEnable: Boolean){
        if (saveButton.isEnabled){
            updateImageButton.isEnabled = false
            takePhotoButton.isEnabled = false
            textProductName.isEnabled = false
            textProductDescription.isEnabled = false
            textCategory.isEnabled = false
            textQuantity.isEnabled = false
            saveButton.isEnabled = false
        } else {
            updateImageButton.isEnabled = isEnable
            takePhotoButton.isEnabled = isEnable
            textProductName.isEnabled = isEnable
            textProductDescription.isEnabled = isEnable
            textCategory.isEnabled = isEnable
            textQuantity.isEnabled = isEnable
            saveButton.isEnabled = isEnable
        }
    }

    private fun initializeVariables(view: View) {
        textProductName = view.findViewById(R.id.productName)
        productImage = view.findViewById(R.id.imageProduit)
        textProductDescription = view.findViewById(R.id.productDescription)
        textCategory = view.findViewById(R.id.productCategory)
        textQuantity = view.findViewById(R.id.productQuantity)
        saveButton = view.findViewById(R.id.saveButton)
        deleteButton = view.findViewById(R.id.deleteItem)
        updateImageButton = view.findViewById(R.id.changeImageButton)
        takePhotoButton = view.findViewById(R.id.takePhotoButton)
    }

    private fun takeImageFromDevice() =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uriSelect: Uri? ->
            uriSelect?.let {
                requireContext().contentResolver.takePersistableUriPermission(
                    it, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                productImage.setImageURI(it)
                imageUri = it
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
            this.imageUri = imageUri
        }
    }

    private fun createUriPhoto(): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val photoFile = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "IMG_$timeStamp.jpg")
        return FileProvider.getUriForFile(requireContext(), "ca.qc.bdeb.c5gm.photoapp", photoFile)
    }

    private fun updateItems() {
        val updatedName = textProductName.text.toString().takeIf { it.isNotBlank() }
        val updatedDescription = textProductDescription.text.toString().takeIf { it.isNotBlank() }
        val updatedCategory = textCategory.text.toString().takeIf { it.isNotBlank() }
        val updatedQuantity = textQuantity.text.toString().toIntOrNull()
        val updatedImageUri = imageUri?.toString()

        if (updatedName.isNullOrBlank() || updatedDescription.isNullOrBlank() || updatedCategory.isNullOrBlank() || updatedQuantity == null || updatedImageUri.isNullOrBlank()) {
            Snackbar.make(requireView(), "Veuillez remplir tous les champs", Snackbar.LENGTH_INDEFINITE).show()
        } else {
            lifecycleScope.launch(Dispatchers.IO) {
                val database = Database_Epicerie.getDatabase(requireContext())
                val currentItem = database.GroceryDAO().findByName(itemName)
                currentItem?.let {
                    val updatedItem = it.copy(
                        nameProduct = updatedName,
                        description = updatedDescription,
                        category = updatedCategory,
                        quantity = updatedQuantity,
                        foodImageURI = updatedImageUri
                    )
                    database.GroceryDAO().updateEpicerie(updatedItem)
                }
                withContext(Dispatchers.Main) {
                    Snackbar.make(requireView(), "Produit mis à jour", Snackbar.LENGTH_SHORT).show()
                    requireActivity().onBackPressed()
                }
            }
        }
    }

    private fun deleteItem() {
        lifecycleScope.launch(Dispatchers.IO) {
            val database = Database_Epicerie.getDatabase(requireContext())
            val itemDelete = database.GroceryDAO().getEpicerieId(productId)
            database.GroceryDAO().deleteEpicerie(itemDelete!!)
            withContext(Dispatchers.Main) {
                Snackbar.make(requireView(), "Produit supprimé", Snackbar.LENGTH_SHORT).show()
                requireActivity().onBackPressed()
            }
        }
    }
}
