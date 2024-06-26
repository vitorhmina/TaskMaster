package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmaster.retrofit.ApiService
import com.example.taskmaster.retrofit.RetrofitClient.getApiServiceWithAuth
import com.example.taskmaster.retrofit.UserProject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Update_User_Project : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private var projectId = 0
    private var email = ""
    private var userProjectId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_assign_to_project)
        apiService = getApiServiceWithAuth(this)
        userProjectId = intent.getIntExtra("userProjectId", -1)

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            val intent = Intent(this@Update_User_Project, UserProjects::class.java)
            intent.putExtra("projectId", projectId)
            startActivity(intent)
        }

        // Fetch user project details by ID and populate UI fields
        fetchUserProjectDetails()

        val buttonUpdate = findViewById<Button>(R.id.buttonUpdateUserProject)
        buttonUpdate.setOnClickListener {
            updateUserProject()
        }
    }

    private fun fetchUserProjectDetails() {
        apiService.getUserProjectById(userProjectId).enqueue(object : Callback<UserProject> {
            override fun onResponse(call: Call<UserProject>, response: Response<UserProject>) {
                if (response.isSuccessful) {
                    val userProject = response.body()
                    if (userProject != null) {
                        findViewById<EditText>(R.id.editTextRole).setText(userProject.role)
                        findViewById<EditText>(R.id.editTextRating).setText(userProject.rating)
                        email = userProject.email ?: ""
                        projectId = userProject.projectId
                    } else {
                        Toast.makeText(this@Update_User_Project, "User Project not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Update_User_Project, "Failed to fetch User Project details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProject>, t: Throwable) {
                Toast.makeText(this@Update_User_Project, "Network error", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Failed to fetch User Project details", t)
            }
        })
    }

    private fun navigateToUserProjectsActivity() {
        val intent = Intent(this@Update_User_Project, UserProjects::class.java)
        intent.putExtra("projectId", projectId)
        startActivity(intent)
    }

    private fun updateUserProject() {
        val role = findViewById<EditText>(R.id.editTextRole).text.toString()
        val rating = findViewById<EditText>(R.id.editTextRating).text.toString()

        if (email.isBlank()) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val userProject = UserProject(
            id = userProjectId,
            name = null,
            email = email,
            role = role,
            rating = rating,
            projectId = projectId
        )

        Log.d(TAG, "User Project update request: $userProject")

        // Make API call to update user project
        apiService.updateUserProject(userProjectId, userProject).enqueue(object : Callback<UserProject?> {
            override fun onResponse(call: Call<UserProject?>, response: Response<UserProject?>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Update_User_Project, "User Project updated successfully", Toast.LENGTH_SHORT).show()
                    navigateToUserProjectsActivity()
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e(TAG, "Failed to update User Project: ${response.code()} - $errorBody")
                    Toast.makeText(this@Update_User_Project, "Failed to update User Project: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProject?>, t: Throwable) {
                Toast.makeText(this@Update_User_Project, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Network error", t)
            }
        })
    }

    companion object {
        private const val TAG = "Update_User_Project"
    }
}
