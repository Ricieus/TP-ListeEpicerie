package com.example.tp_listeepicerie.fragment

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.Database_Epicerie
import com.example.tp_listeepicerie.R
import com.example.tp_listeepicerie.Table_Grocery
import com.example.tp_listeepicerie.recyclerCart.CartAdaptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class list_cart : Fragment() {
    private lateinit var recyclerViewCart: RecyclerView
    private var cartItems: MutableList<Table_Grocery> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewCart = view.findViewById(R.id.recycleCart)

        val orientation = resources.configuration.orientation
        //Permet de changer la configuration de la page
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val gridLayoutManagerPanier = GridLayoutManager(requireContext(), 4)
            recyclerViewCart.layoutManager = gridLayoutManagerPanier
        } else {
            val gridLayoutManagerPanier = GridLayoutManager(requireContext(), 2)
            recyclerViewCart.layoutManager = gridLayoutManagerPanier
        }


        val database = Database_Epicerie.getDatabase(requireContext())
        lifecycleScope.launch(Dispatchers.IO) {
            cartItems = database.GroceryDAO().getAllPanier()
            launch(Dispatchers.Main) {
                refreshRecyclerView()
            }
        }
    }

    fun refreshRecyclerView() {
        recyclerViewCart.adapter = CartAdaptor(requireContext(), this@list_cart, cartItems)
        recyclerViewCart.adapter?.notifyDataSetChanged()
    }

    fun ajoutPanier(item: Table_Grocery) {
        val database = Database_Epicerie.getDatabase(requireContext())
        lifecycleScope.launch(Dispatchers.IO) {
            val existingItem = database.GroceryDAO().findByName(item.nameProduct)

            if (existingItem != null) {
                val itemPanier = Table_Grocery(
                    uid = item.uid,
                    nameProduct = item.nameProduct,
                    quantity = item.quantity,
                    foodImageURI = item.foodImageURI,
                    category = item.category,
                    description = item.description,
                    isCart = true,
                    isFavorite = item.isFavorite
                )
                database.GroceryDAO().updateEpicerie(itemPanier)

                withContext(Dispatchers.Main) {
                    cartItems = database.GroceryDAO().getAllPanier()
                    refreshRecyclerView()
                }
            }
        }
    }

    fun removeFromPanier(item: Table_Grocery) {
        val database = Database_Epicerie.getDatabase(requireContext())
        lifecycleScope.launch(Dispatchers.IO) {

            val itemProduct = Table_Grocery(
                uid = item.uid,
                nameProduct = item.nameProduct,
                quantity = item.quantity,
                foodImageURI = item.foodImageURI,
                category = item.category,
                description = item.description,
                isCart = false,
                isFavorite = item.isFavorite
            )
            database.GroceryDAO().updateEpicerie(itemProduct)
            withContext(Dispatchers.Main) {
                cartItems = database.GroceryDAO().getAllPanier()
                refreshRecyclerView()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val database = Database_Epicerie.getDatabase(requireContext())
        lifecycleScope.launch(Dispatchers.IO) {
            cartItems = database.GroceryDAO().getAllPanier()
            launch(Dispatchers.Main) {
                refreshRecyclerView()
            }
        }
    }

    fun addItemToFavorite(item: Table_Grocery) {
        val database = Database_Epicerie.getDatabase(requireContext())
        lifecycleScope.launch(Dispatchers.IO) {

            val itemProduct = Table_Grocery(
                uid = item.uid,
                nameProduct = item.nameProduct,
                quantity = item.quantity,
                foodImageURI = item.foodImageURI,
                category = item.category,
                description = item.description,
                isCart = item.isCart,
                isFavorite = true
            )
            database.GroceryDAO().updateEpicerie(itemProduct)
        }
    }

    fun removeItemFromFavorite(item: Table_Grocery) {
        val database = Database_Epicerie.getDatabase(requireContext())

        lifecycleScope.launch(Dispatchers.IO) {

            val itemProduct = Table_Grocery(
                uid = item.uid,
                nameProduct = item.nameProduct,
                quantity = item.quantity,
                foodImageURI = item.foodImageURI,
                category = item.category,
                description = item.description,
                isCart = item.isCart,
                isFavorite = false
            )
            database.GroceryDAO().updateEpicerie(itemProduct)
        }
    }
}