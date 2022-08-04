package com.example.mysalon.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.mysalon.R
import com.example.mysalon.databinding.ActivityMainBinding
import com.example.mysalon.model.remote.data.login.LoginResponse
import com.example.mysalon.view.LoginActivity.Companion.LOGIN_INFO
import com.example.mysalon.viewModel.MainViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel
    lateinit var drawerLayout: DrawerLayout
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        Log.e("intent.extras", intent.extras.toString())
        var info = intent.extras?.get(LOGIN_INFO)
        if(info != null) {
            var loginInfo = info as LoginResponse
            mainViewModel.setUserLiveData(loginInfo)
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView

        navigationView.setNavigationItemSelectedListener{ menuItem->
            when(menuItem.itemId){
                R.id.mi_logout->{

                }
                else -> {
                    menuItem.onNavDestinationSelected(findNavController(R.id.my_nav_host_fragment))
                            || super.onOptionsItemSelected(menuItem)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
            true

        }

        val navView = navigationView.inflateHeaderView(R.layout.nav_view_header);

        val tvNavHeaderName: TextView = navView.findViewById(R.id.tv_nav_header_name)
        tvNavHeaderName.text = mainViewModel.userLiveData.value?.fullName
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        val navController = host.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.home_dest),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.e("onBackPressed", "${item.itemId}")
        val current: Fragment? = supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment)
        current?.let {
            Log.e("Fragment", "${it.id}")
        }
        if (item.itemId == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onSupportNavigateUp(): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
        return findNavController(R.id.my_nav_host_fragment).navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}