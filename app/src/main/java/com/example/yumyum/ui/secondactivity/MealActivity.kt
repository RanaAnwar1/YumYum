package com.example.yumyum.ui.secondactivity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.yumyum.R
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.yumyum.databinding.ActivityMealBinding
import com.example.yumyum.ui.firstactivity.MainActivity
import com.example.yumyum.util.Constant
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MealActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController

    private lateinit var binding: ActivityMealBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.title = "Hi, Name"
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_bottom_navigation) as NavHostFragment
        navController = navHost.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_favorite, R.id.navigation_about)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavigationView = binding.navView
        val bottomAppBar: BottomAppBar = binding.bottomAppBar
        val fab: FloatingActionButton = binding.fab
        bottomNavigationView.setupWithNavController(navController)
        fab.setOnClickListener {
            navController.navigate(R.id.navigation_search)
        }

        bottomAppBar.setNavigationOnClickListener {
            Toast.makeText(this, "BottomAppBar Navigation Clicked", Toast.LENGTH_SHORT).show()
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home, R.id.navigation_favorite -> {
                    bottomAppBar.visibility = View.VISIBLE
                }
                else -> {
                    bottomAppBar.visibility = View.GONE
                }
            }
        }
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    if (navController.currentDestination?.id != R.id.navigation_home) {
                        navController.navigate(R.id.navigation_home)
                    }
                    true
                }
                R.id.navigation_favorite -> {
                    if (navController.currentDestination?.id != R.id.navigation_favorite) {
                        navController.navigate(R.id.navigation_favorite)
                    }
                    true
                }
                else -> false
            }

        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_search -> {
                    bottomNavigationView.menu.setGroupCheckable(0, false, true)
                }

                else -> {
                    bottomNavigationView.menu.setGroupCheckable(0, true, true)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.navigation_about -> {
                navController.navigate(R.id.navigation_about)
                true
            }
            R.id.navigation_signout -> {
                Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show()
                navigateToUserActivity()
                finish()
                Constant.REQUESTED_SIGN_OUT = true
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun navigateToUserActivity(){
        Intent(this,MainActivity::class.java).apply {
            startActivity(this)
        }
    }
}