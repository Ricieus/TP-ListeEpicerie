package com.example.tp_listeepicerie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.activityViewModels

class fragment_search_category : Fragment() {

    private val viewModel: GroceryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameEditTxt: EditText = view.findViewById(R.id.nameEditTxt)
        nameEditTxt.setText(viewModel.search)

        val searchBtn: Button = view.findViewById(R.id.searchBtn)
        searchBtn.setOnClickListener {
            viewModel.search = nameEditTxt.text.toString()
            viewModel.search()
        }
    }
}