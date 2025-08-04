package com.x7ree.Toolsbox.ui.dashboard.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
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
    private lateinit var switchDefaultDisplay: SwitchMaterial
    private lateinit var btnSave: MaterialButton
    private lateinit var btnCancel: MaterialButton
    private lateinit var tvSwitchOff: TextView
    private lateinit var tvSwitchOn: TextView
    
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
        setupClickListeners()
        
        val title = if (isEditMode) R.string.edit_tool else R.string.add_tool
        
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(view)
            .setCancelable(false)  // 禁止点击外部关闭
            .create()
        
        // 禁止点击外部关闭对话框
        dialog.setCanceledOnTouchOutside(false)
        
        return dialog
    }
    
    private fun initViews(view: View) {
        etName = view.findViewById(R.id.et_tool_name)
        etDescription = view.findViewById(R.id.et_tool_description)
        etUrl = view.findViewById(R.id.et_tool_url)
        etSortOrder = view.findViewById(R.id.et_sort_order)
        switchDefaultDisplay = view.findViewById(R.id.switch_default_display)
        btnSave = view.findViewById(R.id.btn_save)
        btnCancel = view.findViewById(R.id.btn_cancel)
        tvSwitchOff = view.findViewById(R.id.tv_switch_off)
        tvSwitchOn = view.findViewById(R.id.tv_switch_on)
    }
    
    private fun setupData() {
        toolItem?.let { item ->
            etName.setText(item.name)
            etDescription.setText(item.description)
            etUrl.setText(item.url)
            etSortOrder.setText(item.sortOrder.toString())
            switchDefaultDisplay.isChecked = item.isDefault
        }
        updateSwitchTextColors(switchDefaultDisplay.isChecked)
    }
    
    private fun setupClickListeners() {
        btnCancel.setOnClickListener {
            dismiss()
        }
        
        btnSave.setOnClickListener {
            saveToolItem()
        }
        
        // 监听开关状态变化，更新文字颜色
        switchDefaultDisplay.setOnCheckedChangeListener { _, isChecked ->
            updateSwitchTextColors(isChecked)
        }
    }
    
    private fun updateSwitchTextColors(isChecked: Boolean) {
        if (isChecked) {
            tvSwitchOff.setTextColor(resources.getColor(android.R.color.darker_gray, null))
            tvSwitchOn.setTextColor(resources.getColor(R.color.purple_primary, null))
        } else {
            tvSwitchOff.setTextColor(resources.getColor(R.color.purple_primary, null))
            tvSwitchOn.setTextColor(resources.getColor(android.R.color.darker_gray, null))
        }
    }
    
    private fun saveToolItem() {
        val name = etName.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val url = etUrl.text.toString().trim()
        val sortOrderStr = etSortOrder.text.toString().trim()
        val isDefault = switchDefaultDisplay.isChecked
        
        // 验证字段，如果有错误不关闭对话框
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
            if (sortOrderStr.isEmpty()) 0 else sortOrderStr.toInt()
        } catch (e: NumberFormatException) {
            Toast.makeText(context, R.string.please_enter_valid_sort_order, Toast.LENGTH_SHORT).show()
            return
        }
        
        val newToolItem = if (isEditMode) {
            toolItem!!.copy(
                name = name,
                description = description,
                url = url,
                sortOrder = sortOrder,
                isDefault = isDefault
            )
        } else {
            ToolItem(
                name = name,
                description = description,
                url = url,
                sortOrder = sortOrder,
                isDefault = isDefault
            )
        }
        
        listener?.onToolItemSave(newToolItem, isEditMode)
        dismiss()
    }
    
    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}