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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.x7ree.Toolsbox.R
import com.x7ree.Toolsbox.data.model.ToolItem
import com.x7ree.Toolsbox.databinding.FragmentDashboardBinding
import com.x7ree.Toolsbox.ui.dashboard.adapter.ToolItemAdapter
import com.x7ree.Toolsbox.ui.dashboard.dialog.AddEditToolDialog

class DashboardFragment : Fragment(), AddEditToolDialog.OnToolItemSaveListener {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var toolItemAdapter: ToolItemAdapter

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
            }
        )
        
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
        val dialog = AddEditToolDialog.newInstance(toolItem)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}