package com.example.tp_listeepicerie.fragment

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
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
import com.example.tp_listeepicerie.recyclerItem.ItemAdaptor
import com.example.tp_listeepicerie.widget.ItemListWidget
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class list_product : Fragment() {
    private lateinit var groceryViewModel: GroceryViewModel
    private lateinit var recyclerView: RecyclerView
    private var groceryList: MutableList<Table_Grocery> = mutableListOf()

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Aidé par ChatGPT
        groceryViewModel = ViewModelProvider(requireActivity())[GroceryViewModel::class.java]
        recyclerView = view.findViewById(R.id.recyclerItem)

        //Permet de changer la configuration de la page
        val gridLayoutManagerItem = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = gridLayoutManagerItem

        groceryViewModel.groceryList.observe(viewLifecycleOwner) { updatedGroceryList ->
            recyclerView.adapter = ItemAdaptor(requireContext(), this@list_product, updatedGroceryList.toMutableList())
            recyclerView.adapter?.notifyDataSetChanged()
            notifyWidgetUpdate(requireContext())
        }

        auth = Firebase.auth
    }

    // Function depreciated (NO USE)
    fun refreshRecyclerView() {
        recyclerView.adapter = ItemAdaptor(requireContext(), this@list_product, groceryList)
        recyclerView.adapter?.notifyDataSetChanged()
    }


    fun ajoutPanier(item: Table_Grocery) {
        val database = Database_Epicerie.getDatabase(requireContext())
        lifecycleScope.launch(Dispatchers.IO) {
            // item.copy would allow directly to edit the variable MANBIR (DO YOU UNDERSTAND?)
            database.GroceryDAO().updateEpicerie(item.copy(isCart = true))
            // Here is where you update the information of each list. Same thing as refreshRecyclerView did
            groceryViewModel.updateCartItems(requireContext())
            groceryViewModel.updateGroceryList(requireContext())
        }
    }

    // Function depreciated (NO USE)
    override fun onResume() {
        super.onResume()
        val user = auth.currentUser
        val database = Database_Epicerie.getDatabase(requireContext())
        lifecycleScope.launch(Dispatchers.IO) {
            groceryList = database.GroceryDAO().getAllProductUser(user?.email.toString())
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
                isFavorite = true,
                currentUser = item.currentUser
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
                isFavorite = false,
                currentUser = item.currentUser
            )
            database.GroceryDAO().updateEpicerie(itemProduct)
        }
    }

    fun notifyWidgetUpdate(context: Context) {
        val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE).apply {
            component = ComponentName(context, ItemListWidget::class.java)
        }
        context.sendBroadcast(intent)
    }
}