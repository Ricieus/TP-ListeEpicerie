package com.example.tp_listeepicerie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tp_listeepicerie.page.PageAddItem
import com.example.tp_listeepicerie.page.PageDevelopers
import com.example.tp_listeepicerie.page.PageFavorite
import com.example.tp_listeepicerie.page.PageList
import com.example.tp_listeepicerie.page.PageSettings
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var pageDevelopers: View
    private lateinit var productList: View
    private lateinit var productFavorite: View
    private lateinit var pageSettings: View
    private lateinit var nameMainPage: TextView

    private lateinit var adView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.home_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        MobileAds.initialize(this@MainActivity)
        adView = findViewById(R.id.ad_ViewBanner)

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        nightMode()
        initializeViewPages()

        setListeners()

        auth = Firebase.auth
        nameMainPage = findViewById(R.id.helloName)
    }

    override fun onResume() {
        super.onResume()
        val user = auth.currentUser
        val db = Firebase.firestore

        if (user != null) {
            val userId = user.uid
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        nameMainPage.text = document.getString("firstName")
                    }
                }
        }
    }


    private fun setListeners() {

        pageDevelopers.setOnClickListener {
            val intent = Intent(this@MainActivity, PageDevelopers::class.java)
            this@MainActivity.startActivity(intent)
        }

        productList.setOnClickListener {
            val intent = Intent(this@MainActivity, PageList::class.java)
            this@MainActivity.startActivity(intent)
        }

        productFavorite.setOnClickListener {
            val intent = Intent(this@MainActivity, PageFavorite::class.java)
            this@MainActivity.startActivity(intent)
        }
        pageSettings.setOnClickListener {
            val intent = Intent(this@MainActivity, PageSettings::class.java)
            this@MainActivity.startActivity(intent)
        }
    }


    private fun initializeViewPages() {
        pageDevelopers = findViewById(R.id.developerPage)
        productList = findViewById(R.id.fruit)
        productFavorite = findViewById(R.id.favoris)
        pageSettings = findViewById(R.id.settings)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Permet de changer le mode de l'application
    private fun nightMode() {
        //https://www.youtube.com/watch?v=AHsggyb0vGw&t=52s
        val sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        val nightMode = sharedPreferences.getBoolean("night", false)

        if (!nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
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
