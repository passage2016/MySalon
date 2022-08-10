package com.example.mysalon.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
import com.bumptech.glide.Glide
import com.example.mysalon.R
import com.example.mysalon.databinding.ActivityMainBinding
import com.example.mysalon.model.remote.Constants.BASE_IMAGE_URL
import com.example.mysalon.model.remote.data.login.LoginResponse
import com.example.mysalon.view.LoginActivity.Companion.LOGIN_INFO
import com.example.mysalon.view.fragment.HomeFragmentDirections
import com.example.mysalon.view.fragment.ProductsFragmentDirections
import com.example.mysalon.viewModel.MainViewModel
import com.google.android.material.navigation.NavigationView


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
        if (info != null) {
            var loginInfo = info as LoginResponse
            mainViewModel.userLiveData.value = loginInfo
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mi_logout -> {
                    mainViewModel.logout()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                R.id.mi_share -> {
                    val intent = Intent()
                    intent.action = "android.intent.action.SEND"
                    intent.putExtra("android.intent.extra.SUBJECT", getString(R.string.app_name))
                    intent.putExtra(
                        "android.intent.extra.TEXT",
                        "Hey check out our shop app at: https://play.google.com/store/apps/details"
                    )
                    intent.type = "text/plain"
                    startActivity(intent)
                    (findViewById<View>(R.id.drawer_layout) as DrawerLayout).closeDrawer(
                        GravityCompat.START
                    )
                    true
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
        val ivNavHeaderPicture: ImageView = navView.findViewById(R.id.iv_user_image)
        ivNavHeaderPicture.setOnClickListener {
            val action = HomeFragmentDirections.updateAction()

            findNavController(R.id.fragment_home).navigate(action)
            drawerLayout.closeDrawer(GravityCompat.START)
        }


        tvNavHeaderName.text = mainViewModel.userLiveData.value!!.fullName
        Glide.with(applicationContext)
            .load(mainViewModel.userLiveData.value!!.profilePic)
            .into(ivNavHeaderPicture)
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        mainViewModel.dashboardLiveData.observe(this){
            tvNavHeaderName.text = mainViewModel.userLiveData.value!!.fullName
            Glide.with(applicationContext)
                .load(mainViewModel.userLiveData.value!!.profilePic)
                .into(ivNavHeaderPicture)
        }

        val navController = host.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.home_dest),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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