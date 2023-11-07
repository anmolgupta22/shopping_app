package com.example.shoppingdemo

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppingdemo.ui.ProductListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.window.statusBarColor = Color.TRANSPARENT
        this.window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.productListFragment)
        // Check if the current fragment is the home screen
        if (currentFragment is ProductListFragment) {
            // If on the home screen, finish the activity (close the app)
            finish()
        } else {
            super.onBackPressed()
        }
    }
}