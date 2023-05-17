package com.example.travelmemories

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.travelmemories.databinding.ActivityMainBinding
import com.example.travelmemories.ui.home.HomeFragmentDirections
import com.example.travelmemories.ui.home.HomeViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var binder: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        binder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binder.root)

        setSupportActionBar(binder.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binder.drawerLayout
        val navView: NavigationView = binder.navView
        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_aboutus, R.id.nav_contactus, R.id.nav_share,
                R.id.nav_settings, R.id.nav_logout
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // set the add new travel memory button
        binder.appBarMain.fab.setOnClickListener {
            navController.navigate(R.id.nav_addmemory)
            // prevent creating unrelated fragments
            binder.appBarMain.fab.hide()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        binder.appBarMain.fab.show()
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}