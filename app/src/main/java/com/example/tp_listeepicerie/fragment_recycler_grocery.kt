package com.example.tp_listeepicerie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.groceryAPI.GroceryListAdaptor


class fragment_recycler_grocery : Fragment() {
    private val viewModel: GroceryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recycler_grocery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.groceryRecycler)
        val adapter = GroceryListAdaptor(view.context, emptyList())
        recyclerView.adapter = adapter

        viewModel.groceryResult.observe(viewLifecycleOwner) { result ->
            adapter.groceryList = result
            adapter.notifyDataSetChanged()
        }
    }
}