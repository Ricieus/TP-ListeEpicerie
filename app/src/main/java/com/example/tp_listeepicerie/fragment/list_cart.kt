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


class list_cart : Fragment() {
    private lateinit var groceryViewModel: GroceryViewModel
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

        // Aidé par ChatGPT
        groceryViewModel = ViewModelProvider(requireActivity())[GroceryViewModel::class.java]
        recyclerViewCart = view.findViewById(R.id.recycleCart)

        val gridLayoutManagerItem = GridLayoutManager(requireContext(), 2)
        recyclerViewCart.layoutManager = gridLayoutManagerItem

        groceryViewModel.cartItems.observe(viewLifecycleOwner) { updatedCartItems ->
            cartItems = updatedCartItems.toMutableList()
            refreshRecyclerView()
        }

        groceryViewModel.updateCartItems(requireContext())
    }

    // Function depreciated (NO USE)
    fun refreshRecyclerView() {
        recyclerViewCart.adapter = CartAdaptor(requireContext(), this@list_cart, cartItems)
        recyclerViewCart.adapter?.notifyDataSetChanged()
    }

    fun removeFromPanier(item: Table_Grocery) {
        val database = Database_Epicerie.getDatabase(requireContext())
        lifecycleScope.launch(Dispatchers.IO) {
            // item.copy would allow directly to edit the variable MANBIR (DO YOU UNDERSTAND?)
            database.GroceryDAO().updateEpicerie(item.copy(isCart = false))
            // Here is where you update the information of each list. Same thing as refreshRecyclerView did
            groceryViewModel.updateCartItems(requireContext())
            groceryViewModel.updateGroceryList(requireContext())
        }
    }

    // Function depreciated (NO USE)
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
}