package com.example.digitalheroes


import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.digitalheroes.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val mBottomSheetBh = BottomSheetBehavior.from(binding.aboutSheet)
        window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        window.navigationBarColor = resources.getColor(R.color.colorAccent)


        drawerLayout = binding.drawerLayout
        setDefaultNightMode(MODE_NIGHT_YES)

        mBottomSheetBh.state = BottomSheetBehavior.STATE_HIDDEN

        binding.navView.setNavigationItemSelectedListener {
            if (it.itemId == R.id.aboutFragment) {
                mBottomSheetBh.state = BottomSheetBehavior.STATE_EXPANDED
                Log.i("hyus", "Thesla : ${it.itemId}")
                drawerLayout.closeDrawer(Gravity.LEFT)
                animateLayout()
                return@setNavigationItemSelectedListener false
            } else return@setNavigationItemSelectedListener false
        }

        binding.dismissButton.setOnClickListener {
            mBottomSheetBh.state = BottomSheetBehavior.STATE_HIDDEN
        }

        val navController = this.findNavController(R.id.nav_host_fragment)


        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

//        NavigationUI.setupWithNavController(binding.navView, navController)


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    private fun animateLayout(){
        val colorFrom = resources.getColor(R.color.colorAccent)
        val colorTo = resources.getColor(R.color.colorPrimaryDark)
        val colorLast = resources.getColor(R.color.colorPrimary)
        val colorAnimation =
            ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo, colorLast, colorFrom)
        colorAnimation.duration = 2500 // milliseconds
        colorAnimation.repeatCount = ValueAnimator.INFINITE
        colorAnimation.repeatMode = ValueAnimator.REVERSE

        colorAnimation.addUpdateListener {
                animator ->
            binding.bottomSheetLayout.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()
    }


}


