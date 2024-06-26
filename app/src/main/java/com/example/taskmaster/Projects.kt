package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmaster.retrofit.Project
import com.example.taskmaster.retrofit.ApiService
import com.example.taskmaster.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import android.widget.Toast

class Projects : AppCompatActivity(), ProjectAdapter.ProjectItemClickListener {
    private lateinit var apiService: ApiService
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.projects)

        apiService = RetrofitClient.getApiServiceWithAuth(this)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch projects from API
        fetchProjects()
    }

    private fun fetchProjects() {
        apiService.getUserProjects().enqueue(object : Callback<List<Project>> {
            override fun onResponse(call: Call<List<Project>>, response: Response<List<Project>>) {
                if (response.isSuccessful) {
                    val projectList = response.body()
                    projectList?.let {
                        val adapter = ProjectAdapter(it, this@Projects)
                        recyclerView.adapter = adapter
                    }
                } else {
                    Log.e("Projects", "Failed to fetch projects: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Project>>, t: Throwable) {
                Log.e("Projects", "API call failed", t)
            }
        })
    }

    override fun onItemClicked(projectId: Int) {
        val intent = Intent(this, Tasks::class.java)
        intent.putExtra("projectId", projectId)
        startActivity(intent)
    }

    override fun onUsersClicked(projectId: Int) {
        val intent = Intent(this, UserProjects::class.java)
        intent.putExtra("projectId", projectId)
        startActivity(intent)
    }


    //NOT CORRECT FIX WHEN USER TYPE CONTEXT ADDED
    override fun onUpdateProject(projectId: Int) {
        val intent = Intent(this, Loading2::class.java)
        intent.putExtra("projectId", projectId)
        startActivity(intent)
    }

    //NOT CORRECT FIX WHEN USER TYPE CONTEXT ADDED
    override fun onDeleteProject(projectId: Int) {
        apiService.deleteUser(projectId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Projects, "User deleted successfully", Toast.LENGTH_SHORT).show()
                    fetchProjects()
                } else {
                    // Log the response details
                    val errorBody = response.errorBody()?.string()
                    Log.e("Users", "Failed to delete user: ${response.code()} ${response.message()} $errorBody")
                    Toast.makeText(this@Projects, "Failed to delete user", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Log the throwable message
                Log.e("Users", "Network error: ${t.message}", t)
                Toast.makeText(this@Projects, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
