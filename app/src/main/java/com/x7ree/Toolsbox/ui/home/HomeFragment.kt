package com.x7ree.Toolsbox.ui.home

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.x7ree.Toolsbox.R
import com.x7ree.Toolsbox.data.model.ToolItem
import com.x7ree.Toolsbox.databinding.FragmentHomeBinding
import com.x7ree.Toolsbox.ui.home.adapter.ToolDrawerAdapter

import android.util.Log

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var toolDrawerAdapter: ToolDrawerAdapter
    private lateinit var animatedMenuDrawable: AnimatedMenuDrawable
    
    // 剪贴板内容缓存
    private var clipboardContent: String = ""
    private var shouldAutoFillClipboard: Boolean = true
    private var currentToolItem: ToolItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    
    override fun onResume() {
        super.onResume()
        Log.d("Clipboard", "HomeFragment onResume")
        // 应用从后台恢复时，重新读取剪贴板内容并尝试自动填入
        if (shouldAutoFillClipboard && currentToolItem != null && binding.webview.url != null) {
            Log.d("Clipboard", "应用从后台恢复，重新读取剪贴板内容")
            readSystemClipboard()
            if (clipboardContent.isNotEmpty()) {
                autoFillClipboardContent(binding.webview)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("Clipboard", "HomeFragment onCreateView 开始")
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        
        setupWebView()
        setupDrawer()
        observeData()
        setupAnimatedMenu()
        
        // 读取系统剪贴板内容
        readSystemClipboard()
        
        // 设置默认标题
        activity?.title = "工具箱"
        Log.d("Clipboard", "HomeFragment onCreateView 完成")
        
        return binding.root
    }
    
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        // 设置动画菜单图标
        val menuItem = menu.findItem(R.id.action_open_drawer)
        menuItem.icon = animatedMenuDrawable
        super.onCreateOptionsMenu(menu, inflater)
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_open_drawer -> {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                } else {
                    openDrawer()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        Log.d("Clipboard", "开始设置WebView")
        binding.webview.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
            
            // 允许文件访问和内容访问
            settings.allowFileAccess = true
            settings.allowContentAccess = true
            settings.allowFileAccessFromFileURLs = true
            settings.allowUniversalAccessFromFileURLs = true
            
            // 启用混合内容模式（允许HTTPS页面加载HTTP资源）
            settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            
            // 针对 Android 10 及更高版本的额外配置
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                settings.forceDark = android.webkit.WebSettings.FORCE_DARK_OFF
            }
            
            // 设置用户代理，模拟桌面浏览器以获得更好的兼容性
            settings.userAgentString = settings.userAgentString + " Chrome/91.0.4472.124"
            
            // 添加 JavaScript 接口以支持剪贴板操作
            addJavascriptInterface(ClipboardJavaScriptInterface(requireContext()), "AndroidClipboard")
            
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    Log.d("Clipboard", "页面开始加载: $url")
                    binding.progressBar.visibility = View.VISIBLE
                }
                
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.progressBar.visibility = View.GONE
                    Log.d("Clipboard", "页面加载完成: $url")
                    
                    // 页面加载完成后，重新读取剪贴板内容并自动填入
                    if (shouldAutoFillClipboard) {
                        readSystemClipboard()
                        if (clipboardContent.isNotEmpty()) {
                            autoFillClipboardContent(view)
                        }
                    }
                }
                
                override fun onReceivedError(
                    view: WebView?,
                    errorCode: Int,
                    description: String?,
                    failingUrl: String?
                ) {
                    super.onReceivedError(view, errorCode, description, failingUrl)
                    Log.e("Clipboard", "页面加载错误: $errorCode, $description, failingUrl: $failingUrl")
                    binding.progressBar.visibility = View.GONE
                    // 可以在这里显示错误页面
                }
            }
            
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    // 可以在这里更新进度条
                }
                
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    Log.d("Clipboard", "页面标题: $title")
                    // 可以在这里更新标题
                }
                
                // 处理权限请求，允许访问剪贴板等系统资源
                override fun onPermissionRequest(request: PermissionRequest?) {
                    request?.let {
                        Log.d("Clipboard", "处理权限请求")
                        // 自动授予所有权限请求，包括剪贴板访问
                        // 这样可以确保网页能够访问所需的系统功能
                        it.grant(it.resources)
                    }
                }
            }
        }
        Log.d("Clipboard", "WebView设置完成")
    }
    
    private fun setupDrawer() {
        toolDrawerAdapter = ToolDrawerAdapter { toolItem ->
            homeViewModel.selectTool(toolItem)
        }
        
        binding.recyclerViewDrawerTools.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = toolDrawerAdapter
        }
        
        // 设置刷新按钮点击事件
        binding.layoutRefresh.setOnClickListener {
            refreshCurrentPage()
            // 点击后关闭侧边栏
            binding.drawerLayout.closeDrawer(GravityCompat.END)
        }
        
        // 监听抽屉状态变化以更新菜单图标
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                animatedMenuDrawable.setProgress(slideOffset)
            }

            override fun onDrawerOpened(drawerView: View) {
                animatedMenuDrawable.setProgress(1f)
            }

            override fun onDrawerClosed(drawerView: View) {
                animatedMenuDrawable.setProgress(0f)
            }

            override fun onDrawerStateChanged(newState: Int) {}
        })
        
        // 监听抽屉状态
        homeViewModel.isDrawerOpen.observe(viewLifecycleOwner) { isOpen ->
            if (isOpen) {
                binding.drawerLayout.openDrawer(GravityCompat.END)
            } else {
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            }
        }
    }
    
    private fun setupAnimatedMenu() {
        // 创建动画菜单图标，使用更适合ActionBar的尺寸
        animatedMenuDrawable = AnimatedMenuDrawable(56, 56)
        
        // 设置抽屉点击监听器，用于切换动画状态
        binding.navView.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            }
        }
    }
    
    private fun observeData() {
        // 观察工具列表
        homeViewModel.toolItems.observe(viewLifecycleOwner) { toolItems ->
            toolDrawerAdapter.submitList(toolItems)
            
            // 显示或隐藏空状态
            if (toolItems.isEmpty()) {
                binding.tvEmptyState.visibility = View.VISIBLE
                binding.webview.visibility = View.GONE
            } else {
                binding.tvEmptyState.visibility = View.GONE
                binding.webview.visibility = View.VISIBLE
            }
        }
        
        // 观察当前选中的工具
        homeViewModel.currentToolItem.observe(viewLifecycleOwner) { toolItem ->
            toolItem?.let {
                loadUrl(it.url)
                toolDrawerAdapter.setSelectedTool(it.id)
                // 保存当前工具项
                currentToolItem = it
                // 输出工具信息日志
                Log.d("Clipboard", "当前工具: ${it.name}, URL: ${it.url}")
                if (it.clipboardTargetId.isNotEmpty()) {
                    Log.d("Clipboard", "工具配置了剪贴板目标文本框ID: ${it.clipboardTargetId}")
                } else {
                    Log.d("Clipboard", "工具未配置剪贴板目标文本框ID")
                }
                // 更新Activity的标题
                activity?.title = it.name
            }
        }
    }
    
    /**
     * 加载指定URL
     */
    private fun loadUrl(url: String) {
        try {
            Log.d("Clipboard", "开始加载URL: $url")
            // 确保URL格式正确
            val formattedUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
                "https://$url"
            } else {
                url
            }
            Log.d("Clipboard", "格式化后的URL: $formattedUrl")
            binding.webview.loadUrl(formattedUrl)
        } catch (e: Exception) {
            Log.e("Clipboard", "加载URL时出错", e)
            e.printStackTrace()
            // 处理URL加载错误
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        binding.webview.destroy()
        _binding = null
    }
    
    // 处理返回键，如果抽屉打开则关闭抽屉
    fun onBackPressed(): Boolean {
        return if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
            true
        } else {
            false
        }
    }
    
    // 打开侧滑菜单
    fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.END)
    }
    
    /**
     * 读取系统剪贴板内容
     */
    private fun readSystemClipboard() {
        try {
            val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = clipboardManager.primaryClip
            if (clipData != null && clipData.itemCount > 0) {
                clipboardContent = clipData.getItemAt(0).text?.toString() ?: ""
                Log.d("Clipboard", "成功获取系统剪贴板内容: $clipboardContent")
            } else {
                Log.d("Clipboard", "剪贴板为空或无法访问")
            }
        } catch (e: Exception) {
            Log.e("Clipboard", "读取剪贴板内容时出错", e)
            e.printStackTrace()
        }
    }
    
    /**
    * 自动填入剪贴板内容到指定的文本框
    */
   private fun autoFillClipboardContent(webView: WebView?) {
       currentToolItem?.let { toolItem ->
           val targetId = toolItem.clipboardTargetId
           if (targetId.isNotEmpty()) {
               // 转义剪贴板内容以安全地嵌入到JavaScript字符串中
               val escapedContent = escapeJavaScriptString(clipboardContent)
               // 如果工具配置了剪贴板目标ID，则填入到指定的文本框
               val javascript = "document.getElementById('$targetId').value = '$escapedContent';"
               Log.d("Clipboard", "尝试将剪贴板内容写入指定ID的文本框: $targetId, 内容: $clipboardContent")
               webView?.evaluateJavascript(javascript, null)
           } else {
               // 如果没有配置目标ID，则不执行任何操作
               Log.d("Clipboard", "未配置目标ID，不执行自动填入操作")
           }
       }
   }
   
   /**
    * 转义字符串以安全地嵌入到JavaScript字符串中
    */
   private fun escapeJavaScriptString(str: String): String {
       return str.replace("\\", "\\\\")  // 转义反斜杠
           .replace("'", "\\'")          // 转义单引号
           .replace("\"", "\\\"")        // 转义双引号
           .replace("\n", "\\n")         // 转义换行符
           .replace("\r", "\\r")         // 转义回车符
           .replace("\t", "\\t")         // 转义制表符
   }
    
    /**
     * 刷新当前页面
     */
    private fun refreshCurrentPage() {
        try {
            Log.d("HomeFragment", "刷新当前页面")
            binding.webview.reload()
        } catch (e: Exception) {
            Log.e("HomeFragment", "刷新页面时出错", e)
            e.printStackTrace()
        }
    }
    
    /**
     * JavaScript 接口类，用于处理剪贴板操作
     */
    private inner class ClipboardJavaScriptInterface(private val context: Context) {
        
        @JavascriptInterface
        fun readClipboard(): String {
            return try {
                val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = clipboardManager.primaryClip
                if (clipData != null && clipData.itemCount > 0) {
                    clipData.getItemAt(0).text?.toString() ?: ""
                } else {
                    ""
                }
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
        
        @JavascriptInterface
        fun writeClipboard(text: String): Boolean {
            return try {
                val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("text", text)
                clipboardManager.setPrimaryClip(clipData)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}