package com.ByteMe.cashify_budget_app

import android.content.Intent
import android.os.Bundle
import android.text.util.Linkify
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Registration : AppCompatActivity() {

    private lateinit var CashifyDbAuth: FirebaseAuth
    private lateinit var CashifyDbStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        CashifyDbAuth = FirebaseAuth.getInstance()
        CashifyDbStore = FirebaseFirestore.getInstance()

        val linkTextView = findViewById<TextView>(R.id.linkText)

        val usernameBox = findViewById<EditText>(R.id.usernameTextBox)
        val emailBox = findViewById<EditText>(R.id.emailTextBox)
        val passwordBox = findViewById<EditText>(R.id.passwordTextBox)
        val confirmPasswordBox = findViewById<EditText>(R.id.confirmPasswordTextBox)
        val termsAndConditionsBox = findViewById<CheckBox>(R.id.termsAndConditionCheckBox)
        val registerButton = findViewById<AppCompatButton>(R.id.registerButton)


        linkTextView.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        registerButton.setOnClickListener {
            val username = usernameBox.text.toString().trim()
            val email = emailBox.text.toString().trim()
            val password = passwordBox.text.toString().trim()
            val confirmPassword = confirmPasswordBox.text.toString().trim()
            val hasAcceptedTerms = termsAndConditionsBox.isChecked

            if(username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please complete all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(password != confirmPassword) {
                confirmPasswordBox.error = "Passwords do not match."
                return@setOnClickListener
            }

            if (password < 6.toString()){
                passwordBox.error = "Password must be at least 6 characters long."
                return@setOnClickListener
            }

            if(!hasAcceptedTerms) {
                Toast.makeText(this, "Please accept the terms and conditions.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CashifyDbAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        val userId = CashifyDbAuth.currentUser?.uid ?: return@addOnCompleteListener

                        val userMap = hashMapOf(
                            "username" to username,
                            "email" to email,
                            "accptedTerms" to hasAcceptedTerms
                        )

                        CashifyDbStore.collection("users").document(userId)
                            .set(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Registration successful.", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, Login::class.java)
                                startActivity(intent)
                                finish()
                            }
                    } else {
                        Toast.makeText(this, "Registration failed.", Toast.LENGTH_SHORT).show()
                    }
                }




        }

    }

}