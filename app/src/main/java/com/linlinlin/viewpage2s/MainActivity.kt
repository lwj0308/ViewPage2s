package com.linlinlin.viewpage2s

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.linlinlin.viewpage2s.databinding.ActivityMainBinding
import com.linlinlin.viewpage2s.fragment.FindFragment
import com.linlinlin.viewpage2s.fragment.HomeFragment
import com.linlinlin.viewpage2s.fragment.MeFragment
import com.linlinlin.viewpage2s.fragment.RoundFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val fragments = arrayListOf(HomeFragment(), FindFragment(), RoundFragment(), MeFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /* val navHostFragment =
             supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
         val navController = navHostFragment.navController
         val appBarConfiguration = AppBarConfiguration(binding.bottomNavigationView.menu)
         setupActionBarWithNavController(navController,appBarConfiguration)
         binding.bottomNavigationView.setupWithNavController(navController)*/
        binding.mainViewpager2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = fragments.size
            override fun createFragment(position: Int) = fragments[position]
        }

        binding.mainViewpager2.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomNavigationView.menu.getItem(position).isChecked = true
                binding.mainViewpager2.isUserInputEnabled = binding.mainViewpager2.currentItem > 0
            }
        })

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.homeFragment -> switchFragment(0)
                R.id.findFragment -> switchFragment(1)
                R.id.roundFragment -> switchFragment(2)
                R.id.meFragment -> switchFragment(3)
            }
            true
        }

    }

    public fun switchFragment(position: Int) {
        binding.mainViewpager2.currentItem = position

    }

    public fun getMainViewPager2() = binding.mainViewpager2
}