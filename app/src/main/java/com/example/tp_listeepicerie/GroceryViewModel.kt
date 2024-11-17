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

    fun setCartItems(items: List<Table_Grocery>) {
        _cartItems.value = items
    }

    fun setGroceryList(items: List<Table_Grocery>) {
        _groceryList.value = items
    }

}