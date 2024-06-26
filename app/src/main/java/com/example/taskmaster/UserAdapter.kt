package com.example.taskmaster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmaster.retrofit.User
import android.util.Log
import android.widget.ImageView

class UserAdapter(private val userList: List<User>, private val listener: UserItemClickListener) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    interface UserItemClickListener {
        fun onUpdateUser(userId: Int)
        fun onDeleteUser(userId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.userName.text = currentUser.name
        holder.userType.text = currentUser.userType

        holder.updateIcon.setOnClickListener {
            listener.onUpdateUser(currentUser.id)
        }

        holder.deleteIcon.setOnClickListener {
            listener.onDeleteUser(currentUser.id)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.userName)
        val userType: TextView = itemView.findViewById(R.id.userRole)
        val updateIcon: ImageView = itemView.findViewById(R.id.editIcon)
        val deleteIcon: ImageView = itemView.findViewById(R.id.deleteIcon)

    }
}
