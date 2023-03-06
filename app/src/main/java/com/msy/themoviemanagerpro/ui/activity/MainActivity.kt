package com.msy.themoviemanagerpro.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.msy.themoviemanagerpro.R
import com.msy.themoviemanagerpro.databinding.ActivityMainBinding
import com.msy.themoviemanagerpro.ui.FragmentFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    private var fragmentLabel: String = ""

    private var backButtonCount = 0

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    lateinit var navigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = fragmentFactory
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        navigationView = binding.navigationView

        binding.navigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottommenuSearch -> {
                    navigateBetweenFragment(R.id.searchScreen)
                    true
                }

                R.id.bottommenuWatchlist -> {
                    navigateBetweenFragment(R.id.watchListScreen)
                    true
                }

                R.id.bottommenuFavorites -> {
                    navigateBetweenFragment(R.id.favoriteScreen)
                    true
                }
                else -> false
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                fragmentLabel =
                    binding.fragmentContainerView.findNavController().currentDestination?.label.toString()
                Log.d("semih", fragmentLabel)
                when (fragmentLabel) {
                    "fragment_search_screen" -> {
                        if (backButtonCount >= 1) {
                            finish()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Press the back button once again to close the application.",
                                Toast.LENGTH_SHORT
                            ).show()
                            backButtonCount++
                        }
                    }
                    "fragment_watch_list_screen" -> {
                        backButtonCount = 0
                        navigationView.menu[0].isChecked = true
                        navigateBetweenFragment(R.id.searchScreen)
                    }
                    "fragment_favorite_screen" -> {
                        backButtonCount = 0
                        navigationView.menu[0].isChecked = true
                        navigateBetweenFragment(R.id.searchScreen)
                    }
                    "fragment_detail_screen" -> {
                        backButtonCount = 0
                        val previousFragment =
                            Navigation.findNavController(binding.fragmentContainerView).previousBackStackEntry?.destination?.label

                        previousFragment?.let {
                            when (previousFragment) {
                                "fragment_search_screen" -> {
                                    navigationView.menu[0].isChecked = true
                                    navigateBetweenFragment(R.id.searchScreen)
                                }

                                "fragment_watch_list_screen" -> {
                                    navigationView.menu[1].isChecked = true
                                    navigateBetweenFragment(R.id.watchListScreen)
                                }
                                "fragment_favorite_screen" -> {
                                    navigationView.menu[2].isChecked = true
                                    navigateBetweenFragment(R.id.favoriteScreen)
                                }

                            }
                        }
                    }
                }
            }
        })

    }

    private fun navigateBetweenFragment(id: Int) {
        Navigation.findNavController(binding.fragmentContainerView).navigate(id)
    }

}