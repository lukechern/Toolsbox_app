package com.x7ree.Toolsbox.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.x7ree.Toolsbox.R
import com.x7ree.Toolsbox.data.model.ToolItem

/**
 * 工具项列表适配器
 */
class ToolItemAdapter(
    private val onItemClick: (ToolItem) -> Unit,
    private val onEditClick: (ToolItem) -> Unit,
    private val onDeleteClick: (ToolItem) -> Unit
) : ListAdapter<ToolItem, ToolItemAdapter.ToolItemViewHolder>(ToolItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tool, parent, false)
        return ToolItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToolItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ToolItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tv_tool_name)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.tv_tool_description)
        private val urlTextView: TextView = itemView.findViewById(R.id.tv_tool_url)
        private val sortOrderTextView: TextView = itemView.findViewById(R.id.tv_sort_order)
        private val editButton: View = itemView.findViewById(R.id.btn_edit)
        private val deleteButton: View = itemView.findViewById(R.id.btn_delete)

        fun bind(toolItem: ToolItem) {
            nameTextView.text = toolItem.name
            descriptionTextView.text = toolItem.description
            urlTextView.text = toolItem.url
            sortOrderTextView.text = "排序: ${toolItem.sortOrder}"

            itemView.setOnClickListener { onItemClick(toolItem) }
            editButton.setOnClickListener { onEditClick(toolItem) }
            deleteButton.setOnClickListener { onDeleteClick(toolItem) }
        }
    }

    private class ToolItemDiffCallback : DiffUtil.ItemCallback<ToolItem>() {
        override fun areItemsTheSame(oldItem: ToolItem, newItem: ToolItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ToolItem, newItem: ToolItem): Boolean {
            return oldItem == newItem
        }
    }
}