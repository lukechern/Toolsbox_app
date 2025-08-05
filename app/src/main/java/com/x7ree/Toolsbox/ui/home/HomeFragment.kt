package com.x7ree.Toolsbox.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var toolDrawerAdapter: ToolDrawerAdapter
    private lateinit var animatedMenuDrawable: AnimatedMenuDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        
        setupWebView()
        setupDrawer()
        observeData()
        setupAnimatedMenu()
        
        // 设置默认标题
        activity?.title = "工具箱"
        
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
        binding.webview.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
            
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    binding.progressBar.visibility = View.VISIBLE
                }
                
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.progressBar.visibility = View.GONE
                }
                
                override fun onReceivedError(
                    view: WebView?,
                    errorCode: Int,
                    description: String?,
                    failingUrl: String?
                ) {
                    super.onReceivedError(view, errorCode, description, failingUrl)
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
                    // 可以在这里更新标题
                }
            }
        }
    }
    
    private fun setupDrawer() {
        toolDrawerAdapter = ToolDrawerAdapter { toolItem ->
            homeViewModel.selectTool(toolItem)
        }
        
        binding.recyclerViewDrawerTools.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = toolDrawerAdapter
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
                // 更新Activity的标题
                activity?.title = it.name
            }
        }
    }
    
    private fun loadUrl(url: String) {
        try {
            // 确保URL格式正确
            val formattedUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
                "https://$url"
            } else {
                url
            }
            binding.webview.loadUrl(formattedUrl)
        } catch (e: Exception) {
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
}