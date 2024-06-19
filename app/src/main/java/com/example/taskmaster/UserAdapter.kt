package com.example.taskmaster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmaster.retrofit.User
import android.util.Log

class UserAdapter(private val userList: List<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.userName.text = currentUser.name
        holder.userType.text = getUserType(currentUser.userTypeId)

        Log.d("UserAdapter", "User: ${currentUser.name}, userTypeId: ${currentUser.userTypeId}")

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    private fun getUserType(userTypeId: Int?): String {
        return when (userTypeId) {
            1 -> "Administrator"
            2 -> "Project Manager"
            3 -> "User"
            else -> "Unknown Role"
        }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.userName)
        val userType: TextView = itemView.findViewById(R.id.userRole)
    }
}
