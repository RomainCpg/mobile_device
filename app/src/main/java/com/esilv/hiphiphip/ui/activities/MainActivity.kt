package com.esilv.hiphiphip.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.esilv.hiphiphip.R
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.esilv.hiphiphip.services.BackgroundMusicService
import com.esilv.hiphiphip.ui.extensions.openActivity
import com.esilv.hiphiphip.workers.NotificationWorker

import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scheduleNotification()

        //Utilisation de findViewById pour obtenir le bouton
        val buttonPlay = findViewById<Button>(R.id.buttonPlay)
        val fadeVie = findViewById<View>(R.id.fadeView)
        buttonPlay.setOnClickListener {

            fadeVie.visibility = View.VISIBLE
            val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)

            fadeVie.startAnimation(fadeOut)

            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    // Lancer l'activité une fois que l'animation est terminée
                    openActivity(CinematicActivity::class)
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })

        }

        val buttonSettings = findViewById<Button>(R.id.buttonSettings)

        buttonSettings.setOnClickListener {
            //Settings tab
            openActivity(SettingsActivity::class)
        }

        val intent = Intent(this, BackgroundMusicService::class.java)
        intent.putExtra("MUSIC_RES_ID", R.raw.main_menu_background_music)
        startService(intent)


    }

    fun scheduleNotification() {
        val notificationRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            .setInitialDelay(10, TimeUnit.SECONDS)  // Déclenchement après 20 secondes
            .build()

        // Lancer la notification
        WorkManager.getInstance(applicationContext).enqueue(notificationRequest)

        // Replanifier immédiatement après la fin de l'exécution
        WorkManager.getInstance(applicationContext).getWorkInfoByIdLiveData(notificationRequest.id).observeForever {
            if (it != null && it.state.isFinished) {
                scheduleNotification()  // Relancer le travail après chaque notification
            }
        }
    }

}
