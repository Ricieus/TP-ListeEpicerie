package com.example.tp_listeepicerie.page

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.tp_listeepicerie.MainActivity
import com.example.tp_listeepicerie.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class PageSignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userHaveAccount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_signup)

        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        userHaveAccount = findViewById(R.id.haveAccount)

        val lastNameText = findViewById<EditText>(R.id.lastName)
        val firstNameText = findViewById<EditText>(R.id.firstName)
        val emailText = findViewById<EditText>(R.id.email)
        val passwordText = findViewById<EditText>(R.id.password)
        val confirmPasswordText = findViewById<EditText>(R.id.confirmPassword)
        val signupButton = findViewById<Button>(R.id.signupButton)

        userHaveAccount.setOnClickListener {
            val intent = Intent(this, PageSignIn::class.java)
            startActivity(intent)
        }

        signupButton.setOnClickListener {
            val lastName = lastNameText.text.toString()
            val firstName = firstNameText.text.toString()
            val email = emailText.text.toString()
            val password = passwordText.text.toString()
            val confirmPassword = confirmPasswordText.text.toString()

            if (!checkUserInputSignUp(lastName, firstName, email, password, confirmPassword)) {
                return@setOnClickListener
            }

            createUser(firstName, lastName, email, password)
        }
    }

    private fun checkUserInputSignUp(
        lastName: String,
        firstName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (lastName.isEmpty() || firstName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Snackbar.make(
                findViewById(android.R.id.content),
                "SVP, remplissez tous les champs",
                Snackbar.LENGTH_LONG
            ).show()
            return false
        }

        if (!checkEmail(email)) {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Courriel invalide",
                Snackbar.LENGTH_LONG
            ).show()
            return false
        }

        if (password != confirmPassword) {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Le mot de passe et la confirmation ne sont pas identiques",
                Snackbar.LENGTH_LONG
            ).show()
            return false
        }

        return true
    }

    private fun checkEmail(email: String): Boolean {
        //https://stackoverflow.com/questions/1819142/how-should-i-validate-an-e-mail-address
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    //https://firebase.google.com/docs/auth/android/start
    private fun createUser(firstName: String, lastName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        saveUser(it.uid, firstName, lastName, email)
                    }
                } else {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Échec de l'inscription, vérifiez vos informations",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun goMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun saveUser(uid: String, firstName: String, lastName: String, email: String) {
        val user = mapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email
        )

        firestore.collection("users").document(uid)
            .set(user)
            .addOnSuccessListener {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Inscription réussie",
                    Snackbar.LENGTH_LONG
                ).show()
                goMainActivity()
            }
            .addOnFailureListener { e ->
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Erreur d'inscription",
                    Snackbar.LENGTH_LONG
                ).show()
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
}