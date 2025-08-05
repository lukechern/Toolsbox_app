package com.x7ree.Toolsbox.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 工具项数据模型
 */
@Parcelize
data class ToolItem(
    val id: Long = 0,
    val name: String,           // 工具名称
    val description: String,    // 工具简介
    val url: String,           // 工具网址
    val clipboardTargetId: String = "",  // 剪贴板内容自动填入的目标文本框ID
    val sortOrder: Int,        // 排序序号
    val isDefault: Boolean = false  // 是否默认显示
) : Parcelable