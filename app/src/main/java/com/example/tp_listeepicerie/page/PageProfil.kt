package com.example.tp_listeepicerie.page

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_profil)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        auth = Firebase.auth

        lastNameText = findViewById(R.id.nameText)
        firstNameText = findViewById(R.id.firstNameEdit)
        emailText = findViewById(R.id.emailEdit)
        passwordText = findViewById(R.id.passwordEdit)

        btnSave = findViewById(R.id.btnSaveProfil)
        btnDelete = findViewById(R.id.btnDeleteProfil)
        btnDisconnect = findViewById(R.id.btnDisconnectProfil)


        val user = auth.currentUser
        val db = Firebase.firestore

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
                        Toast.makeText(this, "Aucune donnée trouvée pour cet utilisateur", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Aucun utilisateur connecté", Toast.LENGTH_SHORT).show()
        }

        btnSave.setOnClickListener {
            db.collection("users").document(user!!.uid).update(
                "firstName", firstNameText.text.toString(),
                "lastName", lastNameText.text.toString(),
                "email", emailText.text.toString()
            ).addOnSuccessListener {
                Snackbar.make(findViewById(android.R.id.content), "Profil mis à jour", Snackbar.LENGTH_LONG).show()
            }.addOnFailureListener {
                Snackbar.make(findViewById(android.R.id.content), "Erreur de mise à jour du profil", Snackbar.LENGTH_LONG).show()
            }

            val password = passwordText.text.toString()
            val confirmPassword = passwordText.text.toString()

            if (password.isEmpty() and confirmPassword.isEmpty()){
                if (password != confirmPassword) {
                    Snackbar.make(findViewById(android.R.id.content), "Le mot de passe et la confirmation ne sont pas identiques", Snackbar.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                user.updatePassword(password).addOnSuccessListener {
                    Snackbar.make(findViewById(android.R.id.content), "Mot de passe mis à jour", Snackbar.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Snackbar.make(findViewById(android.R.id.content), "Erreur de mise à jour du mot de passe", Snackbar.LENGTH_LONG).show()
                }
            }
        }

        btnDelete.setOnClickListener {
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

        btnDisconnect.setOnClickListener{
            Firebase.auth.signOut()
            finish()
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