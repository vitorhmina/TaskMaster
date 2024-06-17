package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmaster.retrofit.Repository
import com.example.taskmaster.retrofit.User

class SignUp: AppCompatActivity() {
    private var isPasswordVisible = false
    private var isRepeatPasswordVisible = false
    private var repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        val textPassword = findViewById<EditText>(R.id.textPassword)
        val imageViewPasswordVisibility = findViewById<ImageView>(R.id.imageViewPasswordVisibility)
        val textRepeatPassword = findViewById<EditText>(R.id.textRepeatPassword)
        val imageViewRepeatPasswordVisibility = findViewById<ImageView>(R.id.imageViewRepeatPasswordVisibility)
        val buttonCreateAccount = findViewById<Button>(R.id.buttonCreateAccount)

        super.onCreate (savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.signup)

        buttonBack.setOnClickListener {
            val intent = Intent(this, SignUP_IN::class.java)
            startActivity(intent)
        }

        imageViewPasswordVisibility.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility(textPassword, imageViewPasswordVisibility, isPasswordVisible)
        }

        imageViewRepeatPasswordVisibility.setOnClickListener {
            isRepeatPasswordVisible = !isRepeatPasswordVisible
            togglePasswordVisibility(textRepeatPassword, imageViewRepeatPasswordVisibility, isRepeatPasswordVisible)
        }

        buttonCreateAccount.setOnClickListener {
            val name = findViewById<EditText>(R.id.textName).text.toString()
            val email = findViewById<EditText>(R.id.textEmail).text.toString()
            val password = textPassword.text.toString()
            val repeatPassword = textRepeatPassword.text.toString()

            if (isValidName(name) && isValidEmail(email) && isValidPassword(password) && isPasswordsMatch(password, repeatPassword)) {
                val user = User(0, email, password, name, null, null)

                repository.createAccount(user) { createdAccount, error ->
                    if (error == null) {
                        Toast.makeText(this, getString(R.string.account_create), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SignIn::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, getString(R.string.account_create_failed), Toast.LENGTH_SHORT).show()
                        Log.e("SignUp", "Failed to create account: ${error.message}", error)
                    }
                }
            }
        }
    }

    private fun isValidName(name: String): Boolean {
        return when {
            name.isBlank() -> {
                Toast.makeText(this, getString(R.string.name_blank), Toast.LENGTH_SHORT).show()
                false
            }
            name.length < 2 -> {
                Toast.makeText(this, getString(R.string.name_length), Toast.LENGTH_SHORT).show()
                false
            }
            !name.matches("[a-zA-Z ]+".toRegex()) -> {
                Toast.makeText(this, getString(R.string.name_char), Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return when {
            email.isBlank() -> {
                Toast.makeText(this, getString(R.string.email_blank), Toast.LENGTH_SHORT).show()
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(this, getString(R.string.email_char), Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun isValidPassword(password: String): Boolean {
        return when {
            password.isBlank() -> {
                Toast.makeText(this, getString(R.string.password_blank), Toast.LENGTH_SHORT).show()
                false
            }
            password.length < 6 -> {
                Toast.makeText(this, getString(R.string.password_length), Toast.LENGTH_SHORT).show()
                false
            }
            !password.matches("[a-zA-Z0-9!@#$%^&*()_+=\\-{}|\\[\\]:;\"'<>,.?/\\\\]+".toRegex()) -> {
                Toast.makeText(this, getString(R.string.password_char), Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun isPasswordsMatch(password: String, repeatPassword: String): Boolean {
        return when {
            repeatPassword != password -> {
                Toast.makeText(this, getString(R.string.password_match_failed), Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun togglePasswordVisibility(editText: EditText, imageView: ImageView, isVisible: Boolean) {
        if (isVisible) {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            imageView.setImageResource(R.drawable.visibility)
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            imageView.setImageResource(R.drawable.visibility_off)
        }
        editText.setSelection(editText.text.length)
    }
}