package com.sporyap.sporyap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var materialToolbar: MaterialToolbar
    private lateinit var navHostFragment : NavHostFragment
    private lateinit var navController : NavController
    private lateinit var appBarConfiguration : AppBarConfiguration
    lateinit var bottomNavigationView : BottomNavigationView
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        setupNavController()
        bottomNavigationView.setupWithNavController(navController)
    }

    private fun setupNavController() {
        appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_events , R.id.navigation_message , R.id.navigation_event_add , R.id.navigation_favorite,R.id.navigation_profile))
        setupActionBarWithNavController(navController , appBarConfiguration)
    }

    private fun initView(){
        materialToolbar = findViewById(R.id.activity_main_toolbar)
        progressBar = findViewById(R.id.progress_bar)
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        setSupportActionBar(materialToolbar)
        navController = navHostFragment.navController
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val backIcon = ContextCompat.getDrawable(this, R.drawable.ic_back)
        supportActionBar?.setHomeAsUpIndicator(backIcon)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            android.R.id.home -> {
                navController.navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}