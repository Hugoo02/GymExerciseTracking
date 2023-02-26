package com.hugoo02.gymexercisetracking.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hugoo02.gymexercisetracking.Models.SelectItemModel
import com.hugoo02.gymexercisetracking.R

class ItemListSelectAdapter(private val itemList: List<SelectItemModel>) : RecyclerView.Adapter<ItemListSelectAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textViewItemName)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_select_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.textViewName.text = item.name
        holder.checkBox.isChecked = item.isSelected

        // set an OnClickListener on the item view
        holder.itemView.setOnClickListener {
            // toggle the checkbox
            holder.checkBox.isChecked = !holder.checkBox.isChecked
            // update the selected state of the item
            item.isSelected = holder.checkBox.isChecked
        }

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            item.isSelected = isChecked
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}