package com.x7ree.Toolsbox

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.x7ree.Toolsbox.databinding.ActivityMainBinding
import com.x7ree.Toolsbox.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    
    override fun onBackPressed() {
        // 检查当前是否在HomeFragment，如果是则处理抽屉关闭
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val currentFragment = supportFragmentManager.primaryNavigationFragment
            ?.childFragmentManager?.fragments?.firstOrNull()
        
        if (currentFragment is HomeFragment && currentFragment.onBackPressed()) {
            // HomeFragment处理了返回键（关闭了抽屉）
            return
        }
        
        // 默认返回键处理
        super.onBackPressed()
    }
}