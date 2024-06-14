package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class RecoverPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.recoverpassword)

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            val intent = Intent(this, SingUP_IN::class.java)
            startActivity(intent)
        }
    }
}
