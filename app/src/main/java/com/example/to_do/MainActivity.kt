package com.example.to_do

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.to_do.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView



class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController : NavController

 
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = Navigation.findNavController(this,R.id.nav_host_fragment_content_main)

        setSupportActionBar(binding.appBarMain.toolbar1)
        supportActionBar?.setDisplayShowTitleEnabled(false) // removes title from toolbar


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.



        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navView2 : BottomNavigationView = binding.appBarMain.bottomNav


        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_reminder,R.id.nav_monthlyCalendar, R.id.nav_favourite, R.id.nav_theme,
                R.id.nav_widgets,R.id.nav_donate,R.id.nav_feedback,R.id.nav_FAQ,
                R.id.nav_about_us,R.id.nav_setting,R.id.nav_progress, R.id.nav_dailyCalendar, R.id.nav_weeklyCalendar,
                R.id.nav_profile, R.id.nav_logOut, R.id.nav_invite
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView2.setupWithNavController(navController)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.action_setting -> {

                Navigation.findNavController(this,R.id.nav_host_fragment_content_main).navigate(R.id.nav_setting)
                return true
                }

            R.id.action_invite -> {
                Navigation.findNavController(this,R.id.nav_host_fragment_content_main).navigate(R.id.nav_invite)
                return true
            }

            R.id.action_logOut -> {
                Navigation.findNavController(this,R.id.nav_host_fragment_content_main).navigate(R.id.nav_logOut)
                return true
            }

            R.id.action_profile-> {
                Navigation.findNavController(this,R.id.nav_host_fragment_content_main).navigate(R.id.nav_profile)
                return true
            }

            else -> super.onOptionsItemSelected(item)

        }

    }
}


