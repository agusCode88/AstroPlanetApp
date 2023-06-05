package com.example.astroplanetapp.ui

import android.annotation.SuppressLint
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.Toolbar
import com.example.astroplanetapp.R
import com.example.astroplanetapp.interfaces.IActionBarApp

open class ActionBarActivity : AppCompatActivity(), IActionBarApp {

    lateinit var _toolbar:Toolbar

    override fun loadActionBarInActivity(toolbar: Toolbar) {

        _toolbar = toolbar
        _toolbar.let {
            setSupportActionBar(toolbar)
        }
    }

    override fun loadButtonBackInScreen(value: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(value)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
        return true
    }

    override fun loadItemsOnMenuActionBar(toolbar: Toolbar) {
        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_save -> {
                    //searchInActionBar()
                    true
                }

                else -> false
            }

        }

    }

}