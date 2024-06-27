package com.example.taskmaster.retrofit

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Context
import android.content.SharedPreferences
import android.util.Log

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
                    val userId = response.body()!!.userId
                    val userType = response.body()!!.userType

                    val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putString("token", token)
                        putInt("userId", userId)
                        putString("userType", userType)
                        apply()
                    }

                    Log.d("LoginResponse", "User ID: $userId")
                    Log.d("LoginResponse", "User Type: $userType")

                    callback(true, null)
                } else {
                    callback(false, "Failed to sign in: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback(false, "Failed to sign in: ${t.message}")
            }
        })
    }

    fun logout(context: Context, callback: (Boolean, String?) -> Unit) {
        val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

        with(sharedPreferences.edit()) {
            remove("token")
            remove("userId")
            remove("userType")
            apply()
        }
        callback(true, null)
    }
}