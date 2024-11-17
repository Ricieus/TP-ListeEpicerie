package com.example.tp_listeepicerie.fragment

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.Database_Epicerie
import com.example.tp_listeepicerie.GroceryViewModel
import com.example.tp_listeepicerie.R
import com.example.tp_listeepicerie.Table_Grocery
import com.example.tp_listeepicerie.recyclerCart.CartAdaptor
import com.example.tp_listeepicerie.recyclerItem.ItemAdaptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class list_product : Fragment() {
    private lateinit var groceryViewModel: GroceryViewModel
    private lateinit var recyclerView: RecyclerView
    private var groceryList: MutableList<Table_Grocery> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        groceryViewModel = ViewModelProvider(requireActivity())[GroceryViewModel::class.java]
        recyclerView = view.findViewById(R.id.recyclerItem)

        val orientation = resources.configuration.orientation
        //Permet de changer la configuration de la page
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

            val gridLayoutManagerItem = GridLayoutManager(requireContext(), 4)
            recyclerView.layoutManager = gridLayoutManagerItem
        } else {
            val gridLayoutManagerItem = GridLayoutManager(requireContext(), 2)
            recyclerView.layoutManager = gridLayoutManagerItem
        }

        val database = Database_Epicerie.getDatabase(requireContext())
        lifecycleScope.launch(Dispatchers.IO) {
            groceryList = database.GroceryDAO().getAllProduct()
            launch(Dispatchers.Main) {
                recyclerView.adapter = ItemAdaptor(requireContext(), this@list_product, groceryList)
            }
        }

        groceryViewModel.groceryList.observe(viewLifecycleOwner) { updatedCartItems ->
            groceryList.addAll(updatedCartItems)
            refreshRecyclerView()
        }
    }

    fun refreshRecyclerView() {
        recyclerView.adapter = ItemAdaptor(requireContext(), this@list_product, groceryList)
        recyclerView.adapter?.notifyDataSetChanged()
    }

//    fun ajoutPanier(item: Table_Grocery) {
//        val database = Database_Epicerie.getDatabase(requireContext())
//        lifecycleScope.launch(Dispatchers.IO) {
//            val existingItem = database.GroceryDAO().findByName(item.nameProduct)
//
//            if (existingItem != null) {
//                val itemPanier = Table_Grocery(
//                    uid = item.uid,
//                    nameProduct = item.nameProduct,
//                    quantity = item.quantity,
//                    foodImageURI = item.foodImageURI,
//                    category = item.category,
//                    description = item.description,
//                    isCart = true,
//                    isFavorite = item.isFavorite
//                )
//                database.GroceryDAO().updateEpicerie(itemPanier)
//
//                withContext(Dispatchers.Main) {
//                    groceryList = database.GroceryDAO().getAllProduct()
//                    refreshRecyclerView()
//                }
//            }
//        }
//    }

    fun ajoutPanier(item: Table_Grocery) {
        val database = Database_Epicerie.getDatabase(requireContext())
        lifecycleScope.launch(Dispatchers.IO) {
            database.GroceryDAO().updateEpicerie(item.copy(isCart = true))
            val existingItem = database.GroceryDAO().findByName(item.nameProduct)
            val updatedCartItems = database.GroceryDAO().getAllPanier()

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
                    groceryList = database.GroceryDAO().getAllProduct()
                    groceryViewModel.setCartItems(updatedCartItems)
                    refreshRecyclerView()
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()

        val database = Database_Epicerie.getDatabase(requireContext())
        lifecycleScope.launch(Dispatchers.IO) {
            groceryList = database.GroceryDAO().getAllProduct()
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