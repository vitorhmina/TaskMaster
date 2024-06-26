package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmaster.retrofit.User
import com.example.taskmaster.retrofit.ApiService
import com.example.taskmaster.retrofit.RetrofitClient
import com.example.taskmaster.retrofit.UserProject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProjects : AppCompatActivity(), UserProjectAdapter.UserProjectItemClickListener {

    private lateinit var apiService: ApiService
    private lateinit var recyclerView: RecyclerView
    private var projectId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.user_projects)

        apiService = RetrofitClient.getApiServiceWithAuth(this)

        projectId = intent.getIntExtra("projectId", -1)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)

        buttonBack.setOnClickListener {
            val intent = Intent(this, Projects::class.java)
            startActivity(intent)
        }

        // Set click listener for buttonAdd
        val buttonAdd = findViewById<ImageButton>(R.id.buttonAdd)
        buttonAdd.setOnClickListener {
            val intent = Intent(this, Admin_create_user::class.java)
            startActivity(intent)
        }

        // Fetch users from API
        fetchProjectTeam()
    }

    private fun fetchProjectTeam() {
        apiService.getProjectUsers(projectId).enqueue(object : Callback<List<UserProject>> {
            override fun onResponse(call: Call<List<UserProject>>, response: Response<List<UserProject>>) {
                if (response.isSuccessful) {
                    val userList = response.body()
                    userList?.let {
                        val adapter = UserProjectAdapter(it, this@UserProjects)
                        recyclerView.adapter = adapter
                    }
                } else {
                    // Handle unsuccessful response
                    // Show appropriate error message to the user
                }
            }

            override fun onFailure(call: Call<List<UserProject>>, t: Throwable) {
                // Handle network errors or API call failures
                // Show appropriate error message to the user
            }
        })
    }

    override fun onUpdateUserProject(userProjectId: Int) {
        val intent = Intent(this, Admin_update_user::class.java)
        intent.putExtra("userProjectId", userProjectId)
        startActivity(intent)
    }

    override fun onDeleteUserProject(userProjectId: Int) {
        apiService.deleteUserProject(userProjectId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@UserProjects, "User removed from project successfully", Toast.LENGTH_SHORT).show()
                    fetchProjectTeam()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Users", "Failed to remove user from project: ${response.code()} ${response.message()} $errorBody")
                    Toast.makeText(this@UserProjects, "Failed to remove user from project", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("UserProjects", "Network error: ${t.message}", t)
                Toast.makeText(this@UserProjects, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
