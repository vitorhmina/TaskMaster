package com.example.taskmaster

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import com.example.taskmaster.retrofit.ApiService
import com.example.taskmaster.retrofit.RetrofitClient
import com.example.taskmaster.retrofit.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfile : AppCompatActivity() {

    private lateinit var apiService: ApiService

    private var isPasswordVisible = false
    private var isRepeatPasswordVisible = false

    private var userType: String? = ""
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.edit_profile)

        apiService = RetrofitClient.getApiServiceWithAuth(this)

        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        userId = sharedPreferences.getInt("userId", -1)

        val textPassword = findViewById<EditText>(R.id.textPassword)
        val imageViewPasswordVisibility = findViewById<ImageView>(R.id.imageViewPasswordVisibility)
        val textRepeatPassword = findViewById<EditText>(R.id.textRepeatPassword)
        val imageViewRepeatPasswordVisibility = findViewById<ImageView>(R.id.imageViewRepeatPasswordVisibility)
        val buttonUpdateProfile = findViewById<Button>(R.id.buttonEditProfile)

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        }

        apiService.getUserById(userId).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        findViewById<EditText>(R.id.editTextName).setText(user.name)
                        findViewById<EditText>(R.id.editTextEmail).setText(user.email)

                        userType = user.userType

                    } else {
                        Toast.makeText(this@EditProfile, "User not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@EditProfile, "Failed to fetch user details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@EditProfile, "Network error", Toast.LENGTH_SHORT).show()
                Log.e("Admin_update_user", "Failed to fetch user details", t)
            }
        })

        imageViewPasswordVisibility.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility(textPassword, imageViewPasswordVisibility, isPasswordVisible)
        }

        imageViewRepeatPasswordVisibility.setOnClickListener {
            isRepeatPasswordVisible = !isRepeatPasswordVisible
            togglePasswordVisibility(textRepeatPassword, imageViewRepeatPasswordVisibility, isRepeatPasswordVisible)
        }

        buttonUpdateProfile.setOnClickListener {
            val name = findViewById<EditText>(R.id.editTextName).text.toString()
            val email = findViewById<EditText>(R.id.editTextEmail).text.toString()
            val password = textPassword.text.toString()
            val repeatPassword = textRepeatPassword.text.toString()

            if (isValidName(name) && isValidEmail(email) && isValidPassword(password) && isPasswordsMatch(password, repeatPassword)) {
                val user = User(userId, email, password, name, null, userType)

                Log.d("Admin_update_user", "Updating user with ID $userId: $user")
                val call = apiService.updateUser(userId, user)

                call.enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@EditProfile, "User updated successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@EditProfile, Users::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@EditProfile, "Failed to update user", Toast.LENGTH_SHORT).show()
                        }
                    }



                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(this@EditProfile, "Network error", Toast.LENGTH_SHORT).show()
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