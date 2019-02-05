package com.numero.material_gallery.activity.top_app_bar

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.ContentView
import androidx.appcompat.app.AppCompatActivity
import com.numero.material_gallery.R
import com.numero.material_gallery.extension.getTintedDrawable
import kotlinx.android.synthetic.main.activity_lift_on_scroll.*

@ContentView(R.layout.activity_lift_on_scroll)
class LiftOnScrollActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun createIntent(context: Context): Intent = Intent(context, LiftOnScrollActivity::class.java)
    }

}