package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmaster.retrofit.ApiService
import com.example.taskmaster.retrofit.RetrofitClient
import com.example.taskmaster.retrofit.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Admin_create_user : AppCompatActivity() {

    private lateinit var apiService: ApiService

    private var isPasswordVisible = false
    private var isRepeatPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_update_user)

        apiService = RetrofitClient.getApiServiceWithAuth(this)

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        val textPassword = findViewById<EditText>(R.id.textPassword)
        val imageViewPasswordVisibility = findViewById<ImageView>(R.id.imageViewPasswordVisibility)
        val textRepeatPassword = findViewById<EditText>(R.id.textRepeatPassword)
        val imageViewRepeatPasswordVisibility = findViewById<ImageView>(R.id.imageViewRepeatPasswordVisibility)
        val buttonCreateUser = findViewById<Button>(R.id.buttonCreateUser)

        val userTypes = arrayOf("Administrator", "Project Manager", "User")

        val spinnerUserType = findViewById<Spinner>(R.id.spinnerOptions)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUserType.adapter = adapter

        findViewById<EditText>(R.id.editTextName).text.clear()
        findViewById<EditText>(R.id.editTextEmail).text.clear()
        textPassword.text.clear()
        textRepeatPassword.text.clear()
        spinnerUserType.setSelection(userTypes.indexOf("User"))

        buttonBack.setOnClickListener {
            val intent = Intent(this, Users::class.java)
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

        buttonCreateUser.setOnClickListener {
            val name = findViewById<EditText>(R.id.editTextName).text.toString()
            val email = findViewById<EditText>(R.id.editTextEmail).text.toString()
            val password = textPassword.text.toString()
            val repeatPassword = textRepeatPassword.text.toString()
            val userType = spinnerUserType.selectedItem.toString()

            if (isValidName(name) && isValidEmail(email) && isValidPassword(password) && isPasswordsMatch(password, repeatPassword)) {
                val user = User(0, email, password, name, null, userType)

                val call = apiService.createUser(user)

                call.enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@Admin_create_user, "User created successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@Admin_create_user, Users::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@Admin_create_user, "Failed to update user", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(this@Admin_create_user, "Network error", Toast.LENGTH_SHORT).show()
                    }
                })
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
