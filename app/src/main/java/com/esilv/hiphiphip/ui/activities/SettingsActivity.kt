package com.esilv.hiphiphip.ui.activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.esilv.hiphiphip.R
import com.esilv.hiphiphip.ui.extensions.openActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Bouton pour revenir à la page d'accueil
        val buttonBackToHome = findViewById<Button>(R.id.buttonBackToHome)
        buttonBackToHome.setOnClickListener {
            // Retour à MainActivity
            onBackPressed()
            //openActivity(MainActivity::class)
        }
    }
}
