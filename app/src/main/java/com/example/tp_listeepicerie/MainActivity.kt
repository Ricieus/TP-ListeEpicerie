package com.example.tp_listeepicerie

import android.content.Context
import android.content.Intent
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
import com.example.tp_listeepicerie.page.PageAddItem
import com.example.tp_listeepicerie.page.PageDevelopers
import com.example.tp_listeepicerie.page.PageFavorite
import com.example.tp_listeepicerie.page.PageList
import com.example.tp_listeepicerie.page.PageSettings
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var pageDevelopers: View
    private lateinit var productList: View
    private lateinit var productFavorite: View
    private lateinit var pageSettings: View

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

        val db = Firebase.firestore
//        // Create a new user with a first and last name
//        val user = hashMapOf(
//            "first" to "Ada",
//            "last" to "Lovelace",
//            "born" to 1815
//        )
//
//// Add a new document with a generated ID
//        db.collection("users")
//            .add(user)
//            .addOnSuccessListener { documentReference ->
//                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "Error adding document", e)
//            }
//
//        // Create a new user with a first, middle, and last name
//        val user2 = hashMapOf(
//            "first" to "Alan",
//            "middle" to "Mathison",
//            "last" to "Turing",
//            "born" to 1912
//        )
//
//// Add a new document with a generated ID
//        db.collection("users")
//            .add(user2)
//            .addOnSuccessListener { documentReference ->
//                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "Error adding document", e)
//            }
//
//        db.collection("users")
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    Log.d(TAG, "${document.id} => ${document.data}")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents.", exception)
//            }

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
