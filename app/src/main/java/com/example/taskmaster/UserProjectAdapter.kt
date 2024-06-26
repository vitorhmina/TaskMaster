package com.example.taskmaster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmaster.retrofit.UserProject
import android.util.Log
import android.widget.ImageView

class UserProjectAdapter(private val userProjectList: List<UserProject>, private val listener: UserProjectItemClickListener) : RecyclerView.Adapter<UserProjectAdapter.UserProjectViewHolder>() {

    interface UserProjectItemClickListener {
        fun onUpdateUserProject(userProjectId: Int)
        fun onDeleteUserProject(userProjectId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProjectViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user_project, parent, false)
        return UserProjectViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserProjectViewHolder, position: Int) {
        val currentUserProject = userProjectList[position]
        holder.userName.text = currentUserProject.name
        holder.role.text = currentUserProject.role

        // Handle update user click
        holder.updateIcon.setOnClickListener {
            listener.onUpdateUserProject(currentUserProject.id)
        }

        // Handle delete user click
        holder.deleteIcon.setOnClickListener {
            listener.onDeleteUserProject(currentUserProject.id)
        }
    }

    override fun getItemCount(): Int {
        return userProjectList.size
    }

    class UserProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.userName)
        val role: TextView = itemView.findViewById(R.id.role)
        val updateIcon: ImageView = itemView.findViewById(R.id.editIcon)
        val deleteIcon: ImageView = itemView.findViewById(R.id.deleteIcon)

    }
}
