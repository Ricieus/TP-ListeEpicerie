package com.example.tp_listeepicerie

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.tp_listeepicerie.groceryAPI.API_Items
import com.example.tp_listeepicerie.groceryAPI.GroceryItem
import com.example.tp_listeepicerie.widget.ItemListWidget
import com.example.tp_listeepicerie.widget.ItemListWidgetService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GroceryViewModel() : ViewModel() {
    var search = ""
    var groceryResult = MutableLiveData<List<GroceryItem>>()
    private lateinit var auth: FirebaseAuth
    private val _cartItems = MutableLiveData<List<Table_Grocery>>()
    val cartItems: LiveData<List<Table_Grocery>> get() = _cartItems

    private val _groceryList = MutableLiveData<List<Table_Grocery>>()
    val groceryList: LiveData<List<Table_Grocery>> get() = _groceryList

    // Reupdate the list in cart
    fun updateCartItems(context: Context) {
        auth = Firebase.auth
        val user = auth.currentUser
        val database = Database_Epicerie.getDatabase(context)
        viewModelScope.launch(Dispatchers.IO) {
            val cartList = database.GroceryDAO().getAllPanierUser(user?.email.toString())
            _cartItems.postValue(cartList)
        }
    }

    // Reupdate the list in product
    fun updateGroceryList(context: Context) {
        auth = Firebase.auth
        val user = auth.currentUser
        val database = Database_Epicerie.getDatabase(context)
        viewModelScope.launch(Dispatchers.IO) {
            val productList = database.GroceryDAO().getAllProductUser(user?.email.toString())
            _groceryList.postValue(productList)
        }
    }

    fun search() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = API_Items.apiService.getProducts(search)

            withContext(Dispatchers.Main) {
                groceryResult.postValue(result)
            }
        }
    }
}