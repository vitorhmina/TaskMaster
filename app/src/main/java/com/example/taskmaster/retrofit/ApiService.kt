package com.example.taskmaster.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

public interface ApiService {

    // Authentication
    @POST("auth/signup")
    fun signup(@Body user: User): Call<User>

    @POST("auth/signin")
    fun signin(@Body loginRequest: LoginRequest): Call<LoginResponse>

    // Observations
    @GET("observations")
    fun getObservations(): Call<List<Observation>>

    @GET("observations/{id}")
    fun getObservationById(@Path("id")id: Int): Call<Observation>

    @POST("observations/create")
    fun createObservation(@Body observation: Observation): Call<Observation>

    @PUT("observations/update/{id}")
    fun updateObservation(@Path("id") id: Int, @Body observation: Observation): Call<Observation>

    @DELETE("observations/delete/{id}")
    fun deleteObservation(@Path("id") id: Int): Call<Void>

    // Users
    @GET("users")
    fun getUsers(): Call<List<User>>

    @GET("users/{id}")
    fun getUserById(@Path("id") id: Int): Call<User>

    @POST("users/create")
    fun createUser(@Body user: User): Call<User>

    @PUT("users/update/{id}")
    fun updateUser(@Path("id") id: Int, @Body user: User): Call<User>

    @DELETE("users/delete/{id}")
    fun deleteUser(@Path("id") id: Int): Call<Void>

    // User Projects
    @GET("user_projects/getProjectUsers/{id}")
    fun getProjectUsers(@Path("id") id: Int): Call<List<UserProject>>

    @GET("user_projects/getUserProjects")
    fun getUserProjects(): Call<List<Project>>

    @GET("user_projects/{id}")
    fun getUserProjectById(@Path("id") id: Int): Call<UserProject>

    @POST("user_projects/create")
    fun createUserProject(@Body userProject: UserProject): Call<UserProject>

    @PUT("user_projects/update/{id}")
    fun updateUserProject(@Path("id") id: Int, @Body userProject: UserProject): Call<UserProject>

    @DELETE("user_projects/delete/{id}")
    fun deleteUserProject(@Path("id") id: Int): Call<Void>

    // Projects
    @GET("projects")
    fun getProjects(): Call<List<Project>>

    @GET("projects/{id}")
    fun getProjectById(@Path("id") id: Int): Call<Project>

    @POST("projects/create")
    fun createProject(@Body project: Project): Call<Project>

    @PUT("projects/update/{id}")
    fun updateProject(@Path("id") id: Int, @Body project: Project): Call<Project>

    @DELETE("projects/delete/{id}")
    fun deleteProject(@Path("id") id: Int): Call<Void>

    // User Tasks
    @GET("usertasks")
    fun getUserTasks(): Call<List<UserTask>>

    @GET("usertasks/{id}")
    fun getUserTaskById(@Path("id") id: Int): Call<UserTask>

    @POST("usertasks/create")
    fun createUserTask(@Body userTask: UserTask): Call<UserTask>

    @PUT("usertasks/update/{id}")
    fun updateUserTask(@Path("id") id: Int, @Body userTask: UserTask): Call<UserTask>

    @DELETE("usertasks/delete/{id}")
    fun deleteUserTask(@Path("id") id: Int): Call<Void>

    // Tasks
    @GET("tasks/getProjectTasks/{id}")
    fun getProjectTasks(@Path("id") id: Int): Call<List<Task>>

    @GET("tasks/{id}")
    fun getTaskById(@Path("id") id: Int): Call<Task>

    @POST("tasks/create")
    fun createTask(@Body task: Task): Call<Task>

    @PUT("tasks/update/{id}")
    fun updateTask(@Path("id") id: Int, @Body task: Task): Call<Task>

    @DELETE("tasks/delete/{id}")
    fun deleteTask(@Path("id") id: Int): Call<Void>

}
