package com.example.tp_listeepicerie.page

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_listeepicerie.Database_Epicerie
import com.example.tp_listeepicerie.R
import com.example.tp_listeepicerie.Table_Grocery
import com.example.tp_listeepicerie.recyclerItem.ItemAdaptor
import com.example.tp_listeepicerie.recyclerCart.CartAdaptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PageList : AppCompatActivity() {
    private var cartItems: MutableList<Table_Grocery> = mutableListOf()
//    private lateinit var recyclerViewCart: RecyclerView
//    private lateinit var recyclerView: RecyclerView
    private var groceryList: MutableList<Table_Grocery> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.page_list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.itemAjout -> {
                val intent = Intent(this, PageAddItem::class.java)
                startActivity(intent)
                true
            }

            R.id.HomeButton -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}