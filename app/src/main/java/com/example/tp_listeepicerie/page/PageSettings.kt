package com.example.tp_listeepicerie.page

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.tp_listeepicerie.Database_Epicerie
import com.example.tp_listeepicerie.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sign

class PageSettings : AppCompatActivity() {
    private lateinit var switch: SwitchCompat
    private lateinit var buttonDelete: ImageButton
    private lateinit var profil: Button
    private lateinit var API: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_page_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        switch = findViewById(R.id.switch1)
        buttonDelete = findViewById(R.id.deleteData)
        profil = findViewById(R.id.profilButton)
        API = findViewById(R.id.btnAPI)

        buttonDelete.setOnClickListener {
            deleteData()
        }

        profil.setOnClickListener {
            val intent = Intent(this, PageProfil::class.java)
            startActivity(intent)
        }

        API.setOnClickListener {
            val intent = Intent(this, PageSearch::class.java)
            startActivity(intent)
        }


        setTheme()
    }

    //Permet de changer le thème
    private fun setTheme() {
        //https://www.youtube.com/watch?v=AHsggyb0vGw&t=52s
        val sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val nightMode = sharedPreferences.getBoolean("night", false)

        if (nightMode) {
            switch.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        switch.setOnCheckedChangeListener { switchThemes, isChecked ->
            if (!isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("night", false)
                editor.apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("night", true)
                editor.apply()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.page_settings_menu, menu)
        return super.onCreateOptionsMenu(
            menu
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.goBack -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //Permet de supprimer toutes les données
    private fun deleteData() {
        val database = Database_Epicerie.getDatabase(applicationContext)

        lifecycleScope.launch(Dispatchers.IO) {
            launch(Dispatchers.Main) {
                database.GroceryDAO().deleteAllInformation()
            }
        }
        Snackbar.make(
            findViewById(R.id.main),
            "Tous les données ont été enlevés", Snackbar.LENGTH_LONG)
            .show()
    }
}