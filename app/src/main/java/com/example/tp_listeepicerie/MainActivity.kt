package com.example.tp_listeepicerie

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var view1: View
    private lateinit var productList: View
    private lateinit var productFavorite: View
    private lateinit var pageSettings: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.home_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        nightMode()
        initializeViewPages()

        setListeners()

    }

    private fun setListeners(){
        productList.setOnClickListener {
            val intent = Intent(this@MainActivity, PageList::class.java)
            this@MainActivity.startActivity(intent)
        }

        productFavorite.setOnClickListener{
            val intent = Intent(this@MainActivity, PageFavorite::class.java)
            this@MainActivity.startActivity(intent)
        }
        pageSettings.setOnClickListener {
            val intent = Intent(this@MainActivity, PageSettings::class.java)
            this@MainActivity.startActivity(intent)
        }
    }

    private fun initializeViewPages(){
        view1 = findViewById(R.id.promo_rec)
        productList = findViewById(R.id.fruit)
        productFavorite = findViewById(R.id.favoris)
        pageSettings = findViewById(R.id.settings)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun nightMode(){
        val sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        val nightMode = sharedPreferences.getBoolean("night", false)

        if(!nightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.itemAjout -> {
                val intent = Intent(this, PageAddItem::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
