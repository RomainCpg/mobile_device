package com.esilv.hiphiphip.ui.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.esilv.hiphiphip.R

class TimeFinishedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer_finished)

        val messageTextView: TextView = findViewById(R.id.messageTextView)
        val backButton: Button = findViewById(R.id.backButton)
        val quitButton: Button = findViewById(R.id.quitButton)

        // Afficher un message dans le TextView
        messageTextView.text = "Le temps est écoulé !"

        // Action pour le bouton Retour
        backButton.setOnClickListener {
            // Retour à l'activité précédente
            finish() // Ferme cette activité
        }

        // Action pour le bouton Quitter
        quitButton.setOnClickListener {
            // Ferme l'application
            finishAffinity() // Ferme toutes les activités et quitte l'application
        }
    }
}