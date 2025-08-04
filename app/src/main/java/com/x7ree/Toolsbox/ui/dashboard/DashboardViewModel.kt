package com.x7ree.Toolsbox.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.x7ree.Toolsbox.data.model.ToolItem
import com.x7ree.Toolsbox.data.repository.ToolRepository
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val toolRepository = ToolRepository.getInstance(application)
    
    val toolItems: LiveData<List<ToolItem>> = toolRepository.toolItems.asLiveData()
    
    fun addToolItem(toolItem: ToolItem) {
        viewModelScope.launch {
            toolRepository.addToolItem(toolItem)
        }
    }
    
    fun updateToolItem(toolItem: ToolItem) {
        viewModelScope.launch {
            toolRepository.updateToolItem(toolItem)
        }
    }
    
    fun deleteToolItem(id: Long) {
        viewModelScope.launch {
            toolRepository.deleteToolItem(id)
        }
    }
    
    fun getToolItemById(id: Long): ToolItem? {
        return toolRepository.getToolItemById(id)
    }
}