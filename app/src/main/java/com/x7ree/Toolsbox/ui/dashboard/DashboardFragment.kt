package com.x7ree.Toolsbox.ui.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.x7ree.Toolsbox.R
import com.x7ree.Toolsbox.data.model.ToolItem
import com.x7ree.Toolsbox.databinding.FragmentDashboardBinding
import com.x7ree.Toolsbox.ui.dashboard.adapter.ToolItemAdapter
import com.x7ree.Toolsbox.ui.dashboard.dialog.AddEditToolDialog
import java.util.Collections

class DashboardFragment : Fragment(), AddEditToolDialog.OnToolItemSaveListener {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var toolItemAdapter: ToolItemAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel = ViewModelProvider(this)[DashboardViewModel::class.java]
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        
        setupRecyclerView()
        observeData()
        
        // 设置页面标题
        activity?.title = "配置项"
        
        return binding.root
    }
    
    private fun setupRecyclerView() {
        toolItemAdapter = ToolItemAdapter(
            onItemClick = { toolItem ->
                // 点击工具项，打开网址
                openUrl(toolItem.url)
            },
            onEditClick = { toolItem ->
                // 编辑工具项
                showAddEditDialog(toolItem)
            },
            onDeleteClick = { toolItem ->
                // 删除工具项
                showDeleteConfirmDialog(toolItem)
            },
            onDragClick = { toolItem ->
                // 显示拖拽提示
                showDragHint()
            }
        )
        
        // 初始化 ItemTouchHelper
        val callback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                
                // 交换列表中项目的位置
                val currentList = toolItemAdapter.currentList.toMutableList()
                if (fromPosition < toPosition) {
                    for (i in fromPosition until toPosition) {
                        Collections.swap(currentList, i, i + 1)
                    }
                } else {
                    for (i in fromPosition downTo toPosition + 1) {
                        Collections.swap(currentList, i, i - 1)
                    }
                }
                
                // 更新适配器
                toolItemAdapter.submitList(currentList)
                return true
            }
            
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // 不处理滑动操作
            }
            
            override fun isLongPressDragEnabled(): Boolean {
                return false // 我们通过拖拽图标触发拖拽，而不是长按
            }
            
            override fun isItemViewSwipeEnabled(): Boolean {
                return false // 禁用滑动
            }
            
            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                // 拖拽结束后更新排序序号
                updateSortOrder()
            }
        }
        
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewTools)
        
        // 将 ItemTouchHelper 传递给适配器
        toolItemAdapter.setItemTouchHelper(itemTouchHelper)
        
        binding.recyclerViewTools.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = toolItemAdapter
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_tool -> {
                showAddEditDialog(null)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun observeData() {
        dashboardViewModel.toolItems.observe(viewLifecycleOwner) { toolItems ->
            toolItemAdapter.submitList(toolItems)
            
            // 显示或隐藏空状态
            if (toolItems.isEmpty()) {
                binding.tvEmptyState.visibility = View.VISIBLE
                binding.recyclerViewTools.visibility = View.GONE
            } else {
                binding.tvEmptyState.visibility = View.GONE
                binding.recyclerViewTools.visibility = View.VISIBLE
            }
        }
    }
    
    private fun showAddEditDialog(toolItem: ToolItem?) {
        val toolCount = toolItemAdapter.currentList.size
        val dialog = AddEditToolDialog.newInstance(toolItem, toolCount)
        dialog.show(childFragmentManager, "AddEditToolDialog")
    }
    
    private fun showDeleteConfirmDialog(toolItem: ToolItem) {
        val customView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_delete_confirm, null)
        val btnCancel = customView.findViewById<MaterialButton>(R.id.btn_cancel)
        val btnDelete = customView.findViewById<MaterialButton>(R.id.btn_delete)
        val tvMessage = customView.findViewById<TextView>(R.id.tv_message)

        tvMessage.text = getString(R.string.confirm_delete, toolItem.name)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete_tool)
            .setView(customView)
            .setCancelable(false)
            .show()

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnDelete.setOnClickListener {
            dashboardViewModel.deleteToolItem(toolItem.id)
            Toast.makeText(context, R.string.tool_deleted, Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
    }
    
    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, R.string.cannot_open_url, Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onToolItemSave(toolItem: ToolItem, isEditMode: Boolean) {
        if (isEditMode) {
            dashboardViewModel.updateToolItem(toolItem)
            Toast.makeText(context, R.string.tool_updated, Toast.LENGTH_SHORT).show()
        } else {
            dashboardViewModel.addToolItem(toolItem)
            Toast.makeText(context, R.string.tool_added, Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun showDragHint() {
        // 显示拖拽提示条
        Toast.makeText(context, "按住拖动可给工具项目排序", Toast.LENGTH_SHORT).show()
    }
    
    private fun updateSortOrder() {
        // 更新排序序号
        val currentList = toolItemAdapter.currentList
        for (i in currentList.indices) {
            val toolItem = currentList[i]
            // 创建一个新的 ToolItem 对象，更新 sortOrder
            val updatedToolItem = toolItem.copy(sortOrder = i + 1)
            // 更新数据库中的工具项
            dashboardViewModel.updateToolItem(updatedToolItem)
        }
    }
    


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}