package com.x7ree.Toolsbox.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.x7ree.Toolsbox.data.model.ToolItem
import com.x7ree.Toolsbox.utils.SampleDataHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 工具数据仓库，负责数据的持久化和管理
 */
class ToolRepository private constructor(context: Context) {
    
    companion object {
        private const val PREFS_NAME = "tool_items_prefs"
        private const val KEY_TOOL_ITEMS = "tool_items"
        
        @Volatile
        private var INSTANCE: ToolRepository? = null
        
        fun getInstance(context: Context): ToolRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ToolRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    private val sharedPrefs: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()
    
    private val _toolItems = MutableStateFlow<List<ToolItem>>(emptyList())
    val toolItems: StateFlow<List<ToolItem>> = _toolItems.asStateFlow()
    
    private var nextId = 1L
    
    init {
        loadToolItems()
    }
    
    /**
     * 从SharedPreferences加载工具项
     */
    private fun loadToolItems() {
        val json = sharedPrefs.getString(KEY_TOOL_ITEMS, null)
        if (json != null) {
            val type = object : TypeToken<List<ToolItem>>() {}.type
            val items = gson.fromJson<List<ToolItem>>(json, type)
            _toolItems.value = items.sortedBy { it.sortOrder }
            nextId = (items.maxOfOrNull { it.id } ?: 0) + 1
        }
    }
    
    /**
     * 初始化示例数据（仅在首次使用时）
     */
    fun initializeSampleData() {
        if (_toolItems.value.isEmpty()) {
            val sampleTools = SampleDataHelper.getSampleTools()
            sampleTools.forEach { toolItem ->
                addToolItem(toolItem)
            }
        }
    }
    
    /**
     * 保存工具项到SharedPreferences
     */
    private fun saveToolItems() {
        val json = gson.toJson(_toolItems.value)
        sharedPrefs.edit().putString(KEY_TOOL_ITEMS, json).apply()
    }
    
    /**
     * 添加工具项
     */
    fun addToolItem(toolItem: ToolItem): ToolItem {
        val newItem = toolItem.copy(id = nextId++)
        val currentItems = _toolItems.value.toMutableList()
        currentItems.add(newItem)
        _toolItems.value = currentItems.sortedBy { it.sortOrder }
        saveToolItems()
        return newItem
    }
    
    /**
     * 更新工具项
     */
    fun updateToolItem(toolItem: ToolItem) {
        val currentItems = _toolItems.value.toMutableList()
        val index = currentItems.indexOfFirst { it.id == toolItem.id }
        if (index != -1) {
            currentItems[index] = toolItem
            _toolItems.value = currentItems.sortedBy { it.sortOrder }
            saveToolItems()
        }
    }
    
    /**
     * 删除工具项
     */
    fun deleteToolItem(id: Long) {
        val currentItems = _toolItems.value.toMutableList()
        currentItems.removeAll { it.id == id }
        _toolItems.value = currentItems
        saveToolItems()
    }
    
    /**
     * 根据ID获取工具项
     */
    fun getToolItemById(id: Long): ToolItem? {
        return _toolItems.value.find { it.id == id }
    }
}