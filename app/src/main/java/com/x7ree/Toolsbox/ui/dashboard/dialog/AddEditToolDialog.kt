package com.x7ree.Toolsbox.ui.dashboard.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.x7ree.Toolsbox.R
import com.x7ree.Toolsbox.data.model.ToolItem

/**
 * 添加/编辑工具项对话框
 */
class AddEditToolDialog : DialogFragment() {
    
    companion object {
        private const val ARG_TOOL_ITEM = "tool_item"
        private const val ARG_IS_EDIT_MODE = "is_edit_mode"
        
        fun newInstance(toolItem: ToolItem? = null): AddEditToolDialog {
            val dialog = AddEditToolDialog()
            val args = Bundle()
            args.putParcelable(ARG_TOOL_ITEM, toolItem)
            args.putBoolean(ARG_IS_EDIT_MODE, toolItem != null)
            dialog.arguments = args
            return dialog
        }
    }
    
    interface OnToolItemSaveListener {
        fun onToolItemSave(toolItem: ToolItem, isEditMode: Boolean)
    }
    
    private var listener: OnToolItemSaveListener? = null
    private var toolItem: ToolItem? = null
    private var isEditMode: Boolean = false
    
    private lateinit var etName: EditText
    private lateinit var etDescription: EditText
    private lateinit var etUrl: EditText
    private lateinit var etSortOrder: EditText
    
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnToolItemSaveListener) {
            listener = context
        } else if (parentFragment is OnToolItemSaveListener) {
            listener = parentFragment as OnToolItemSaveListener
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            toolItem = it.getParcelable(ARG_TOOL_ITEM)
            isEditMode = it.getBoolean(ARG_IS_EDIT_MODE, false)
        }
    }
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_edit_tool, null)
        
        initViews(view)
        setupData()
        
        val title = if (isEditMode) R.string.edit_tool else R.string.add_tool
        
        return AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(view)
            .setPositiveButton(R.string.save) { _, _ ->
                saveToolItem()
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }
    
    private fun initViews(view: android.view.View) {
        etName = view.findViewById(R.id.et_tool_name)
        etDescription = view.findViewById(R.id.et_tool_description)
        etUrl = view.findViewById(R.id.et_tool_url)
        etSortOrder = view.findViewById(R.id.et_sort_order)
    }
    
    private fun setupData() {
        toolItem?.let { item ->
            etName.setText(item.name)
            etDescription.setText(item.description)
            etUrl.setText(item.url)
            etSortOrder.setText(item.sortOrder.toString())
        }
    }
    
    private fun saveToolItem() {
        val name = etName.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val url = etUrl.text.toString().trim()
        val sortOrderStr = etSortOrder.text.toString().trim()
        
        if (name.isEmpty()) {
            Toast.makeText(context, R.string.please_enter_tool_name, Toast.LENGTH_SHORT).show()
            return
        }
        
        if (description.isEmpty()) {
            Toast.makeText(context, R.string.please_enter_tool_description, Toast.LENGTH_SHORT).show()
            return
        }
        
        if (url.isEmpty()) {
            Toast.makeText(context, R.string.please_enter_tool_url, Toast.LENGTH_SHORT).show()
            return
        }
        
        val sortOrder = try {
            sortOrderStr.toInt()
        } catch (e: NumberFormatException) {
            Toast.makeText(context, R.string.please_enter_valid_sort_order, Toast.LENGTH_SHORT).show()
            return
        }
        
        val newToolItem = if (isEditMode) {
            toolItem!!.copy(
                name = name,
                description = description,
                url = url,
                sortOrder = sortOrder
            )
        } else {
            ToolItem(
                name = name,
                description = description,
                url = url,
                sortOrder = sortOrder
            )
        }
        
        listener?.onToolItemSave(newToolItem, isEditMode)
    }
    
    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}