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

    @POST("observations")
    fun createObservation(@Body observation: Observation): Call<Observation>

    @PUT("observations/{id}")
    fun updateObservation(@Path("id") id: Int, @Body observation: Observation): Call<Observation>

    @DELETE("observations/{id}")
    fun deleteObservation(@Path("id") id: Int): Call<Void>

    // Users
    @GET("users")
    fun getUsers(): Call<List<User>>

    @GET("users/{id}")
    fun getUserById(@Path("id") id: Int): Call<User>

    @POST("users")
    fun createUser(@Body user: User): Call<User>

    @PUT("users/update/{id}")
    fun updateUser(@Path("id") id: Int, @Body user: User): Call<User>

    @DELETE("users/{id}")
    fun deleteUser(@Path("id") id: Int): Call<Void>

    // User Types
    @GET("usertypes")
    fun getUserTypes(): Call<List<UserType>>

    @GET("usertypes/{id}")
    fun getUserTypeById(@Path("id") id: Int): Call<UserType>

    @POST("usertypes")
    fun createUserType(@Body userType: UserType): Call<UserType>

    @PUT("usertypes/{id}")
    fun updateUserType(@Path("id") id: Int, @Body userType: UserType): Call<UserType>

    @DELETE("usertypes/{id}")
    fun deleteUserType(@Path("id") id: Int): Call<Void>

    // User Projects
    @GET("user_projects/getUserProjects")
    fun getUserProjects(): Call<List<Project>>

    @GET("userprojects")
    fun getUserProjectById(@Path("id") id: Int): Call<UserProject>

    @POST("userprojects")
    fun createUserProject(@Body userProject: UserProject): Call<UserProject>

    @PUT("userprojects/{id}")
    fun updateUserProject(@Path("id") id: Int, @Body userProject: UserProject): Call<UserProject>

    @DELETE("userprojects/{id}")
    fun deleteUserProject(@Path("id") id: Int): Call<Void>

    // Projects
    @GET("projects")
    fun getProjects(): Call<List<Project>>

    @GET("projects/{id}")
    fun getProjectById(@Path("id") id: Int): Call<Project>

    @POST("projects")
    fun createProject(@Body project: Project): Call<Project>

    @PUT("projects/{id}")
    fun updateProject(@Path("id") id: Int, @Body project: Project): Call<Project>

    @DELETE("projects/{id}")
    fun deleteProject(@Path("id") id: Int): Call<Void>

    // User Tasks
    @GET("usertasks")
    fun getUserTasks(): Call<List<UserTask>>

    @GET("usertasks/{id}")
    fun getUserTaskById(@Path("id") id: Int): Call<UserTask>

    @POST("usertasks")
    fun createUserTask(@Body userTask: UserTask): Call<UserTask>

    @PUT("usertasks/{id}")
    fun updateUserTask(@Path("id") id: Int, @Body userTask: UserTask): Call<UserTask>

    @DELETE("usertasks/{id}")
    fun deleteUserTask(@Path("id") id: Int): Call<Void>

    // Tasks
    @GET("tasks")
    fun getTasks(): Call<List<Task>>

    @GET("tasks/{id}")
    fun getTaskById(@Path("id") id: Int): Call<Task>

    @POST("tasks")
    fun createTask(@Body task: Task): Call<Task>

    @PUT("tasks/{id}")
    fun updateTask(@Path("id") id: Int, @Body task: Task): Call<Task>

    @DELETE("tasks/{id}")
    fun deleteTask(@Path("id") id: Int): Call<Void>

}
