package com.cheadtech.popularmovieskt.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.cheadtech.popularmovieskt.R

class PostersSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_posters_settings)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home)
            NavUtils.navigateUpFromSameTask(this)
        return super.onOptionsItemSelected(item)
    }
}