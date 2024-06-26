package com.example.taskmaster

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmaster.retrofit.ApiService
import com.example.taskmaster.retrofit.RetrofitClient.getApiServiceWithAuth
import com.example.taskmaster.retrofit.Task
import com.example.taskmaster.retrofit.User
import com.example.taskmaster.retrofit.UserProject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Assign_User_to_Project : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private var projectId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.assign_to_project)
        apiService = getApiServiceWithAuth(this)
        projectId = intent.getIntExtra("projectId", -1)

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            navigateToUserProjectsActivity()
        }

        val buttonAssignToProject = findViewById<Button>(R.id.buttonAssignToProject)
        buttonAssignToProject.setOnClickListener {
            AssignToProject()
        }
    }

    private fun navigateToUserProjectsActivity() {
        val intent = Intent(this@Assign_User_to_Project, UserProjects::class.java)
        intent.putExtra("projectId", projectId)
        startActivity(intent)
    }

    private fun AssignToProject() {
        val email = findViewById<EditText>(R.id.editTextEmail).text.toString()
        val role = findViewById<EditText>(R.id.editTextRole).text.toString()

        if (projectId == -1) {
            Toast.makeText(this@Assign_User_to_Project, "Invalid project ID", Toast.LENGTH_SHORT).show()
            return
        }

        val userProject = UserProject(0, null, email, role, null, projectId)
        Log.d(TAG, "User Project creation request: $userProject")

        val call = apiService.createUserProject(userProject)
        call.enqueue(object : Callback<UserProject?> {
            override fun onResponse(call: Call<UserProject?>, response: Response<UserProject?>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Assign_User_to_Project, "User assigned successfully", Toast.LENGTH_SHORT).show()
                    navigateToUserProjectsActivity()
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e(TAG, "Failed to assign user: ${response.code()} - $errorBody")
                    Toast.makeText(this@Assign_User_to_Project, "Failed to assign user: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProject?>, t: Throwable) {
                Toast.makeText(this@Assign_User_to_Project, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Network error", t)
            }
        })
    }

    companion object {
        private const val TAG = "Assign_User_To_Project"
    }
}
