package com.x7ree.Toolsbox.utils

import com.x7ree.Toolsbox.data.model.ToolItem

/**
 * 示例数据帮助类
 */
object SampleDataHelper {
    
    /**
     * 获取示例工具数据
     */
    fun getSampleTools(): List<ToolItem> {
        return listOf(
            ToolItem(
                name = "GitHub",
                description = "全球最大的代码托管平台",
                url = "https://github.com",
                sortOrder = 1,
                isDefault = true  // 设置GitHub为默认显示
            ),
            ToolItem(
                name = "Stack Overflow",
                description = "程序员问答社区",
                url = "https://stackoverflow.com",
                sortOrder = 2,
                isDefault = false
            ),
            ToolItem(
                name = "MDN Web Docs",
                description = "Web开发技术文档",
                url = "https://developer.mozilla.org",
                sortOrder = 3,
                isDefault = false
            ),
            ToolItem(
                name = "Android Developers",
                description = "Android官方开发者文档",
                url = "https://developer.android.com",
                sortOrder = 4,
                isDefault = false
            ),
            ToolItem(
                name = "Kotlin官网",
                description = "Kotlin编程语言官方网站",
                url = "https://kotlinlang.org",
                sortOrder = 5,
                isDefault = false
            )
        )
    }
}