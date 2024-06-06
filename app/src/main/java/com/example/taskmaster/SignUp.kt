package com.example.taskmaster

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SignUp: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate (savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.signup)
    }
}