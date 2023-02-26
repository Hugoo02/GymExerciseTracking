package com.hugoo02.gymexercisetracking.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hugoo02.gymexercisetracking.Models.UserModel
import com.hugoo02.gymexercisetracking.R

class UserAdapter(private var simpleList: List<UserModel>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(id: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_simple, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = simpleList[position]

        holder.textViewName.text = user.name

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(simpleList[position].id!!)
        }
    }

    override fun getItemCount(): Int {
        return simpleList.size
    }

    fun updateList(newList: List<UserModel>) {
        simpleList = newList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)

    }
}