package com.example.tp_listeepicerie.page

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tp_listeepicerie.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.sign

class PageSignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_signup)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val lastNameText = findViewById<EditText>(R.id.lastName)
        val firstNameText = findViewById<EditText>(R.id.firstName)
        val emailText = findViewById<EditText>(R.id.email)
        val passwordText = findViewById<EditText>(R.id.password)
        val confirmPasswordText = findViewById<EditText>(R.id.confirmPassword)
        val signupButton = findViewById<Button>(R.id.signupButton)

        signupButton.setOnClickListener {


            val lastName = lastNameText.text.toString()
            val firstName = firstNameText.text.toString()
            val email = emailText.text.toString()
            val password = passwordText.text.toString()
            val confirmPassword = confirmPasswordText.text.toString()
            if (password != confirmPassword) {
                Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
                }

            createUser(firstName, lastName, email, password)
        }
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
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Échec de l'inscription : ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
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
                Toast.makeText(this, "Utilisateur créé avec succès", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Erreur de enregistrement de utilisateur : ${e.message}")
                Toast.makeText(this, "Erreur de enregistrement : ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}