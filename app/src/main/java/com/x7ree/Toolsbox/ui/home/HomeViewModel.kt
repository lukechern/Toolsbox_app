package com.x7ree.Toolsbox.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.x7ree.Toolsbox.data.model.ToolItem
import com.x7ree.Toolsbox.data.repository.ToolRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val toolRepository = ToolRepository.getInstance(application)
    
    val toolItems: LiveData<List<ToolItem>> = toolRepository.toolItems.asLiveData()
    
    private val _currentToolItem = MutableLiveData<ToolItem?>()
    val currentToolItem: LiveData<ToolItem?> = _currentToolItem
    
    private val _isDrawerOpen = MutableLiveData<Boolean>().apply { value = false }
    val isDrawerOpen: LiveData<Boolean> = _isDrawerOpen
    
    init {
        // 监听工具列表变化，自动选择默认工具
        toolItems.observeForever { tools ->
            if (tools.isNotEmpty() && _currentToolItem.value == null) {
                val defaultTool = toolRepository.getDefaultToolItem()
                defaultTool?.let { selectTool(it) }
            }
        }
    }
    
    fun selectTool(toolItem: ToolItem) {
        _currentToolItem.value = toolItem
        closeDrawer()
    }
    
    fun openDrawer() {
        _isDrawerOpen.value = true
    }
    
    fun closeDrawer() {
        _isDrawerOpen.value = false
    }
    
    fun toggleDrawer() {
        _isDrawerOpen.value = !(_isDrawerOpen.value ?: false)
    }
}