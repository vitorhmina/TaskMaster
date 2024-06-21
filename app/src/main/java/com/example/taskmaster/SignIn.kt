package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.taskmaster.retrofit.Repository


class SignIn : AppCompatActivity() {

    private var isPasswordVisible = false
    private var isRepeatPasswordVisible = false
    private var repository = Repository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.signin)

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        val signUpTextView: TextView = findViewById(R.id.signUpText)
        val text = getString(R.string.dont_have_account_signup)
        val spannableString = SpannableString(text)
        val forgotPasswordTextView: TextView = findViewById(R.id.forgotpassword)
        val forgotPasswordText = forgotPasswordTextView.text.toString()
        val forgotPasswordSpannableString = SpannableString(forgotPasswordText)
        val emailEditText = findViewById<EditText>(R.id.textEmail)
        val passwordEditText = findViewById<EditText>(R.id.textPassword)
        val signinButton = findViewById<Button>(R.id.buttonSignIn)
        val textPassword = findViewById<EditText>(R.id.textPassword)
        val imageViewPasswordVisibility = findViewById<ImageView>(R.id.imageViewPasswordVisibility)
        buttonBack.setOnClickListener {
            val intent = Intent(this, SignUP_IN::class.java)
            startActivity(intent)
        }

        imageViewPasswordVisibility.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility(textPassword, imageViewPasswordVisibility, isPasswordVisible)
        }

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@SignIn, SignUp::class.java)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.color = ContextCompat.getColor(this@SignIn, android.R.color.black)
            }
        }

        spannableString.setSpan(clickableSpan, 22, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(android.graphics.Typeface.BOLD), 22, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        signUpTextView.text = spannableString
        signUpTextView.movementMethod = android.text.method.LinkMovementMethod.getInstance()

        val forgotPasswordClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@SignIn, RecoverPassword::class.java)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(this@SignIn, android.R.color.black)
            }
        }

        forgotPasswordSpannableString.setSpan(forgotPasswordClickableSpan, 0, forgotPasswordText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        forgotPasswordSpannableString.setSpan(StyleSpan(android.graphics.Typeface.BOLD), 0, forgotPasswordText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        forgotPasswordTextView.text = forgotPasswordSpannableString
        forgotPasswordTextView.movementMethod = LinkMovementMethod.getInstance()
        forgotPasswordTextView.highlightColor = Color.TRANSPARENT

        signinButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                repository.signIn(this, email, password) { success, errorMessage ->
                    if (success) {
                        Toast.makeText(this, getString(R.string.signin_success), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Loading2::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, getString(R.string.signin_failed), Toast.LENGTH_SHORT).show()
                        Log.e("SignIn", "Failed to sign in: $errorMessage")
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.signin_blank), Toast.LENGTH_SHORT).show()
            }
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
