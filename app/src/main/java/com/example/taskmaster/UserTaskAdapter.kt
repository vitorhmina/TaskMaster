package com.example.taskmaster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmaster.retrofit.UserProject
import android.util.Log
import android.widget.ImageView
import com.example.taskmaster.retrofit.UserTask

class UserTaskAdapter(private val userTaskList: List<UserTask>, private val listener: UserTaskItemClickListener) : RecyclerView.Adapter<UserTaskAdapter.UserTaskViewHolder>() {

    interface UserTaskItemClickListener {
        fun onUpdateUserTask(userTaskId: Int)
        fun onDeleteUserTask(userTaskId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserTaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user_task, parent, false)
        return UserTaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserTaskViewHolder, position: Int) {
        val currentUserTask = userTaskList[position]
        holder.userName.text = currentUserTask.name
        holder.completionRate.text = "Completion Rate: ${currentUserTask.completionRate ?: "0"}%"
        holder.timeSpent.text = "Time Spent: ${currentUserTask.timeSpent ?: "0"} hours"


        holder.updateIcon.setOnClickListener {
            listener.onUpdateUserTask(currentUserTask.id)
        }

        holder.deleteIcon.setOnClickListener {
            listener.onDeleteUserTask(currentUserTask.id)
        }
    }

    override fun getItemCount(): Int {
        return userTaskList.size
    }

    class UserTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.userName)
        val completionRate: TextView = itemView.findViewById(R.id.completionRate)
        val timeSpent: TextView = itemView.findViewById(R.id.timeSpent)
        val updateIcon: ImageView = itemView.findViewById(R.id.editIcon)
        val deleteIcon: ImageView = itemView.findViewById(R.id.deleteIcon)

    }
}
