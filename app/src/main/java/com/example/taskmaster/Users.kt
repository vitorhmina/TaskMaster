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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Users : AppCompatActivity(), UserAdapter.UserItemClickListener {

    private lateinit var apiService: ApiService
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.users)

        apiService = RetrofitClient.getApiServiceWithAuth(this)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val buttonAdd = findViewById<ImageButton>(R.id.buttonAdd)
        buttonAdd.setOnClickListener {
            val intent = Intent(this, Admin_create_user::class.java)
            startActivity(intent)
        }

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        }

        fetchUsers()
    }

    private fun fetchUsers() {
        apiService.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val userList = response.body()
                    userList?.let {
                        val adapter = UserAdapter(it, this@Users)
                        recyclerView.adapter = adapter
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
            }
        })
    }

    override fun onUpdateUser(userId: Int) {
        val intent = Intent(this, Admin_update_user::class.java)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }

    override fun onDeleteUser(userId: Int) {
        apiService.deleteUser(userId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Users, "User deleted successfully", Toast.LENGTH_SHORT).show()
                    fetchUsers()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Users", "Failed to delete user: ${response.code()} ${response.message()} $errorBody")
                    Toast.makeText(this@Users, "Failed to delete user", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Users", "Network error: ${t.message}", t)
                Toast.makeText(this@Users, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
