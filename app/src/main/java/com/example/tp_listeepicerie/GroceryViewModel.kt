package com.example.tp_listeepicerie

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroceryViewModel() : ViewModel() {
    private val _cartItems = MutableLiveData<List<Table_Grocery>>()
    val cartItems: LiveData<List<Table_Grocery>> get() = _cartItems

    private val _groceryList = MutableLiveData<List<Table_Grocery>>()
    val groceryList: LiveData<List<Table_Grocery>> get() = _groceryList

    // Reupdate the list in cart
    fun updateCartItems(context: Context) {
        val database = Database_Epicerie.getDatabase(context)
        viewModelScope.launch(Dispatchers.IO) {
            val cartList = database.GroceryDAO().getAllPanier()
            _cartItems.postValue(cartList)
        }
    }

    // Reupdate the list in product
    fun updateGroceryList(context: Context) {
        val database = Database_Epicerie.getDatabase(context)
        viewModelScope.launch(Dispatchers.IO) {
            val productList = database.GroceryDAO().getAllProduct()
            _groceryList.postValue(productList)
        }
    }
}