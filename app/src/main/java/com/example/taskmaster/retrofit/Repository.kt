package com.example.taskmaster.retrofit

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository {
    private val apiService = RetrofitClient.apiService

    fun createUser(user: User, callback: (User?, Throwable?) -> Unit) {
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
}