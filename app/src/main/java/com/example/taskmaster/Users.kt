package com.example.taskmaster

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmaster.retrofit.User
import com.example.taskmaster.retrofit.ApiService
import com.example.taskmaster.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Users : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.users)

        apiService = RetrofitClient.getApiServiceWithAuth(this)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch users from API
        fetchUsers()
    }

    private fun fetchUsers() {
        apiService.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val userList = response.body()
                    userList?.let {
                        val adapter = UserAdapter(it)
                        recyclerView.adapter = adapter
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                // Handle network errors or API call failures
                // Show appropriate error message to the user
            }
        })
    }
}
