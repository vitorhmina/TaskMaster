package com.example.taskmaster

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class Dashboard : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private var userType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.dashboard)

        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        userType = prefs.getString("userType", null)

        val userCard = findViewById<CardView>(R.id.UserCard)
        val xxxxCard = findViewById<CardView>(R.id.xxxxCard)
        val projectCard = findViewById<CardView>(R.id.projectCard)
        val taskCard = findViewById<CardView>(R.id.TaskCard)

        when (userType) {
            "Administrator" -> {
                Log.d("Dashboard", "Setting visibility for Administrator")
                userCard.visibility = View.VISIBLE
                xxxxCard.visibility = View.VISIBLE
                projectCard.visibility = View.VISIBLE
                taskCard.visibility = View.VISIBLE
            }

            "Project Manager" -> {
                Log.d("Dashboard", "Setting visibility for Project Manager")
                userCard.visibility = View.INVISIBLE
                xxxxCard.visibility = View.INVISIBLE
                projectCard.visibility = View.VISIBLE
                taskCard.visibility = View.VISIBLE
            }

            "User" -> {
                Log.d("Dashboard", "Setting visibility for User")
                userCard.visibility = View.INVISIBLE
                xxxxCard.visibility = View.INVISIBLE
                projectCard.visibility = View.VISIBLE
                taskCard.visibility = View.VISIBLE
            }

            else -> {
                Log.e("Dashboard", "Unknown user type: $userType")
            }
        }

        userCard.setOnClickListener {
            val intent = Intent(this, Users::class.java)
            startActivity(intent)
        }

        xxxxCard.setOnClickListener {
            val intent = Intent(this, AdminProjects::class.java)
            startActivity(intent)
        }

        projectCard.setOnClickListener {
            val intent = Intent(this, Projects::class.java)
            startActivity(intent)
        }

        taskCard.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
        }

        findViewById<View>(R.id.projects).setOnClickListener {
            BottomNavigationHandler.handleNavigationClicks(this, it)
        }
        findViewById<View>(R.id.home).setOnClickListener {
            BottomNavigationHandler.handleNavigationClicks(this, it)
        }
        findViewById<View>(R.id.profile).setOnClickListener {
            BottomNavigationHandler.handleNavigationClicks(this, it)
        }
    }
}
