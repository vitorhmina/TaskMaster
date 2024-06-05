package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SingUP_IN : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate (savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.signup_in)
    }
}