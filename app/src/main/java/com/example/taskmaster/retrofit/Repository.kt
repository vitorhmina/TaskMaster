package com.example.taskmaster.retrofit

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    fun loginUser(loginRequest: LoginRequest, callback: (LoginResponse?, Throwable?) -> Unit) {
        val call = apiService.signin(loginRequest)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        callback(loginResponse, null)
                    }
                } else {
                    callback(null, Throwable("Failed to sign in: ${response.code()} ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback(null, Throwable("Failed to sign in: ${t.message}"))
            }
        })
    }
}