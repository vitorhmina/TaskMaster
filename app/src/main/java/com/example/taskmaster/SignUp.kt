package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SignUp: AppCompatActivity() {

    private var isPasswordVisible = false
    private var isRepeatPasswordVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate (savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.signup)

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            val intent = Intent(this, SignUP_IN::class.java)
            startActivity(intent)
        }

        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val imageViewPasswordVisibility = findViewById<ImageView>(R.id.imageViewPasswordVisibility)
        imageViewPasswordVisibility.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility(editTextPassword, imageViewPasswordVisibility, isPasswordVisible)
        }

        val editTextRepeatPassword = findViewById<EditText>(R.id.editTextRepeatPassword)
        val imageViewRepeatPasswordVisibility = findViewById<ImageView>(R.id.imageViewRepeatPasswordVisibility)
        imageViewRepeatPasswordVisibility.setOnClickListener {
            isRepeatPasswordVisible = !isRepeatPasswordVisible
            togglePasswordVisibility(editTextRepeatPassword, imageViewRepeatPasswordVisibility, isRepeatPasswordVisible)
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