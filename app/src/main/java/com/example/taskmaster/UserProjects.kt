package com.example.taskmaster

import androidx.appcompat.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
        Log.d(TAG, "Received projectId: $projectId")

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)

        buttonBack.setOnClickListener {
            val intent = Intent(this, Projects::class.java)
            startActivity(intent)
        }

        val buttonAdd = findViewById<ImageButton>(R.id.buttonAdd)
        buttonAdd.setOnClickListener {
            val intent = Intent(this, Assign_User_to_Project::class.java)
            intent.putExtra("projectId", projectId)
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
                }
            }

            override fun onFailure(call: Call<List<UserProject>>, t: Throwable) {
            }
        })
    }

    override fun onUpdateUserProject(userProjectId: Int) {
        val intent = Intent(this, Update_User_Project::class.java)
        intent.putExtra("userProjectId", userProjectId)
        startActivity(intent)
    }

    override fun onDeleteUserProject(userProjectId: Int) {
        val builder = AlertDialog.Builder(this@UserProjects)
        builder.setTitle(getString(R.string.confirm_remove_user_title))
        builder.setMessage(getString(R.string.remove_project_msg))

        builder.setPositiveButton(getString(R.string.yes)) { dialog, which ->
            apiService.deleteUserProject(userProjectId).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@UserProjects,
                            getString(R.string.user_removed_from_project_successfully), Toast.LENGTH_SHORT).show()
                        fetchProjectTeam()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("Users", "Failed to remove user from project: ${response.code()} ${response.message()} $errorBody")
                        Toast.makeText(this@UserProjects,
                            getString(R.string.failed_to_remove_user_from_project), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("UserProjects", "Network error: ${t.message}", t)
                    Toast.makeText(this@UserProjects, getString(R.string.no_internet_2), Toast.LENGTH_SHORT).show()
                }
            })
        }

        builder.setNegativeButton(getString(R.string.no)) { dialog, which ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    companion object {
        private const val TAG = "User_Projects"
    }

}
