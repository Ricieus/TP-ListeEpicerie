package com.example.tp_listeepicerie.page

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_profil)

        auth = Firebase.auth

        lastNameText = findViewById(R.id.nameEdit)
        firstNameText = findViewById(R.id.firstNameEdit)
        emailText = findViewById(R.id.emailEdit)
        passwordText = findViewById(R.id.passwordEdit)


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
    }
}