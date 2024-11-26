package com.example.tp_listeepicerie.page

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.tp_listeepicerie.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PageProfil : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var lastNameText: EditText
    private lateinit var firstNameText: EditText
    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button
    private lateinit var btnDisconnect: Button
    private lateinit var db: FirebaseFirestore
    private lateinit var confirmPasswordText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_profil)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        auth = Firebase.auth
        db = Firebase.firestore

        lastNameText = findViewById(R.id.nameText)
        firstNameText = findViewById(R.id.firstNameEdit)
        emailText = findViewById(R.id.emailEdit)
        passwordText = findViewById(R.id.passwordEdit)
        confirmPasswordText = findViewById(R.id.passwordEditConfirm)

        btnSave = findViewById(R.id.btnSaveProfil)
        btnDelete = findViewById(R.id.btnDeleteProfil)
        btnDisconnect = findViewById(R.id.btnDisconnectProfil)


        loadUserInformation()


        saveUserInformation()


        btnDelete.setOnClickListener {
            deleteUserInformation()
        }

        btnDisconnect.setOnClickListener {
            Firebase.auth.signOut()
            finish()
        }
    }


    private fun loadUserInformation() {
        val user = auth.currentUser

        if (user != null) {
            emailText.setText(user.email)

            val userId = user.uid
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document -> //Aidé par ChatGPT
                    if (document.exists()) {
                        firstNameText.setText(document.getString("firstName"))
                        lastNameText.setText(document.getString("lastName"))
                        emailText.setText(document.getString("email"))
                    } else {
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Erreur de récupération des données",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Aucun utilisateur connecté",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun saveUserInformation() {
        val user = auth.currentUser

        btnSave.setOnClickListener {
            val password = passwordText.text.toString()
            val confirmPassword = passwordText.text.toString()

            if (password.isEmpty() || confirmPassword.isEmpty()) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Veuillez remplir les deux champs de mot de passe",
                    Snackbar.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Le mot de passe et la confirmation ne sont pas identiques",
                    Snackbar.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            db.collection("users").document(user!!.uid).update(
                "firstName", firstNameText.text.toString(),
                "lastName", lastNameText.text.toString(),
                "email", emailText.text.toString()
            ).addOnSuccessListener {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Profil mis à jour",
                    Snackbar.LENGTH_LONG
                ).show()
            }.addOnFailureListener {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Erreur de mise à jour du profil",
                    Snackbar.LENGTH_LONG
                ).show()
            }

            user.updatePassword(password).addOnSuccessListener {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Mot de passe mis à jour",
                    Snackbar.LENGTH_LONG
                ).show()
            }.addOnFailureListener {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Erreur de mise à jour du mot de passe",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }


    private fun deleteUserInformation() {
        val user = auth.currentUser

        db.collection("users").document(user!!.uid).delete().addOnSuccessListener {
            user.delete().addOnSuccessListener {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Compte supprimé",
                    Snackbar.LENGTH_LONG
                ).show()
                finish()
            }.addOnFailureListener {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Erreur de suppression du compte",
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