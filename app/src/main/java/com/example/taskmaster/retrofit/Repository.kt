package com.example.taskmaster.retrofit

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Context
import android.content.SharedPreferences

class Repository() {
    private val apiService = RetrofitClient.apiService

    fun createAccount(user: User, callback: (User?, Throwable?) -> Unit) {
        val call = apiService.signup(user)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("Failed to create user: ${response.code()} ${response.message()}"))
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                callback(null, Throwable("Failed to create user: ${t.message}"))
            }
        })
    }

    fun signIn(context: Context, email: String, password: String, callback: (Boolean, String?) -> Unit) {
        val loginRequest = LoginRequest(email, password)
        val call = apiService.signin(loginRequest)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val token = response.body()!!.token
                    context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
                        .edit().putString("token", token).apply()
                    callback(true, null)
                } else {
                    callback(false, Throwable("Failed to create user: ${response.code()} ${response.message()}").toString())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback(false, Throwable("Failed to create user: ${t.message}").toString())
            }
        })
    }
}