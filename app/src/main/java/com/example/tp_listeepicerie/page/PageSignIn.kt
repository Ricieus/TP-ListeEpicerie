package com.example.tp_listeepicerie.page

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.tp_listeepicerie.MainActivity
import com.example.tp_listeepicerie.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class PageSignIn : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var noAccount: TextView
    private lateinit var passwordCheckbox: CheckBox
    private lateinit var emailText: EditText
    private lateinit var signInButton: Button
    private lateinit var passwordText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_signin)

        auth = Firebase.auth
        noAccount = findViewById(R.id.noAccount)
        passwordCheckbox = findViewById(R.id.passwordCheckbox)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (auth.currentUser != null) {
            goMainActivity()
        }

        emailText = findViewById(R.id.email)
        passwordText = findViewById(R.id.password)
        signInButton = findViewById(R.id.signinButton)


        noAccount.setOnClickListener {
            val intent = Intent(this, PageSignUp::class.java)
            startActivity(intent)
        }

        signInButton.setOnClickListener {
            val email = emailText.text.toString()
            val password = passwordText.text.toString()

            if (!checkUserInput(email, password)) {
                return@setOnClickListener
            }

            signInUser(email, password)
        }

        showPassword()
    }

    private fun showPassword() {
        passwordCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                passwordText.inputType = android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                passwordText.inputType =
                    android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            passwordText.setSelection(passwordText.text.length)
        }
    }


    private fun checkUserInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Le champs email ne doit pas être vide",
                Snackbar.LENGTH_LONG
            ).show()
            return false
        }

        if (password.isEmpty()) {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Le champs mot de passe ne doit pas être vide",
                Snackbar.LENGTH_LONG
            ).show()
            return false
        }

        return true
    }

    private fun goMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    //https://firebase.google.com/docs/auth/android/start
    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Authentification réussie",
                        Snackbar.LENGTH_LONG
                    ).show()
//                        finish()
                    goMainActivity()
                } else {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Échec d'authentification, vérifiez vos informations",
                        Snackbar.LENGTH_LONG
                    ).show()
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
}