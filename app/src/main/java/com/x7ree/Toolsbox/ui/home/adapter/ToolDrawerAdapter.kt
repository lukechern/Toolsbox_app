package com.x7ree.Toolsbox.ui.home.adapter

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
 * 工具页面侧滑菜单适配器
 */
class ToolDrawerAdapter(
    private val onToolClick: (ToolItem) -> Unit
) : ListAdapter<ToolItem, ToolDrawerAdapter.ToolDrawerViewHolder>(ToolItemDiffCallback()) {

    private var selectedToolId: Long = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolDrawerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tool_drawer, parent, false)
        return ToolDrawerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToolDrawerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setSelectedTool(toolId: Long) {
        val oldSelectedPosition = currentList.indexOfFirst { it.id == selectedToolId }
        val newSelectedPosition = currentList.indexOfFirst { it.id == toolId }
        
        selectedToolId = toolId
        
        if (oldSelectedPosition != -1) {
            notifyItemChanged(oldSelectedPosition)
        }
        if (newSelectedPosition != -1) {
            notifyItemChanged(newSelectedPosition)
        }
    }

    inner class ToolDrawerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tv_drawer_tool_name)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.tv_drawer_tool_description)
        private val defaultBadgeTextView: TextView = itemView.findViewById(R.id.tv_default_badge)

        fun bind(toolItem: ToolItem) {
            nameTextView.text = toolItem.name
            descriptionTextView.text = toolItem.description
            
            // 显示或隐藏默认工具徽标
            defaultBadgeTextView.visibility = if (toolItem.isDefault) View.VISIBLE else View.GONE

            // 设置选中状态
            itemView.isSelected = toolItem.id == selectedToolId
            itemView.setBackgroundResource(
                if (toolItem.id == selectedToolId) {
                    R.color.selected_item_background_purple
                } else {
                    android.R.color.transparent
                }
            )

            itemView.setOnClickListener {
                onToolClick(toolItem)
            }
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