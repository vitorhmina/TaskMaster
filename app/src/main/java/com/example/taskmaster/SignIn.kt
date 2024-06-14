package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat

class SignIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.signin)

        // Estilizar o TextView
        val signUpTextView: TextView = findViewById(R.id.signUpTextView)
        val text = "Don’t have an account? Sign Up"
        val spannableString = SpannableString(text)

        // Estilizar "Sign Up" em negrito e clicável
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@SignIn, SignUp::class.java)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true // Remove underline
                ds.color = ContextCompat.getColor(this@SignIn, android.R.color.black) // Altera a cor para preto
            }
        }

        spannableString.setSpan(clickableSpan, 22, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(android.graphics.Typeface.BOLD), 22, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        signUpTextView.text = spannableString
        signUpTextView.movementMethod = android.text.method.LinkMovementMethod.getInstance()
        //signUpTextView.highlightColor = Color.TRANSPARENT // Optional: Remove highlight color on click

        val forgotPasswordTextView: TextView = findViewById(R.id.forgotpassword)
        val forgotPasswordText = forgotPasswordTextView.text.toString()
        val forgotPasswordSpannableString = SpannableString(forgotPasswordText)

        // Estilizar "Forgot Password" em negrito e clicável
        val forgotPasswordClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@SignIn, RecoverPassword::class.java)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false // Remove underline
                ds.color = ContextCompat.getColor(this@SignIn, android.R.color.black) // Altera a cor para preto
            }
        }

        forgotPasswordSpannableString.setSpan(forgotPasswordClickableSpan, 0, forgotPasswordText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        forgotPasswordSpannableString.setSpan(StyleSpan(android.graphics.Typeface.BOLD), 0, forgotPasswordText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        forgotPasswordTextView.text = forgotPasswordSpannableString
        forgotPasswordTextView.movementMethod = LinkMovementMethod.getInstance()
        forgotPasswordTextView.highlightColor = Color.TRANSPARENT // Optional: Remove highlight color on click
    }
}
