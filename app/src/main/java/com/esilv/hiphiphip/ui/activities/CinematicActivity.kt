package com.esilv.hiphiphip.ui.activities

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.esilv.hiphiphip.R
import com.esilv.hiphiphip.data.models.SoundPlayer
import com.esilv.hiphiphip.services.BackgroundMusicService
import com.esilv.hiphiphip.ui.extensions.openActivity

class CinematicActivity : AppCompatActivity() {

    // List of images for the slideshow
    private val images = listOf(
        R.drawable.gs1,
        R.drawable.gs2,
        R.drawable.gs3,
        R.drawable.gs4,
        R.drawable.gs5,
    )

    // List of text corresponding to each image
    private val slideTexts = listOf(
        "They said AI would save us.....",    // Text for gs1
        "They didn't tell us it would enslave us. Now humanity is a shadow of it's former self", // Text for gs2
        "....and I am the last one standing.",     // Text for gs3
        "A messaged blinked insistenly on the screen, The sender marked by shroud and anonymity as GHOST ",  // Text for gs4
        "Get ready .... The Nexus - the AI's centeral core. It's vulnerable, but only if you act now. Upload the virus and destroy the AI..... I have marked the coordinates.... Good Luck! "        // Text for gs5
    )

    private var currentIndex = 0
    private lateinit var imageView: ImageView
    private lateinit var slideTextView: TextView
    private lateinit var buttonNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cinematic)



        val intent = Intent(this, BackgroundMusicService::class.java)
        intent.putExtra("MUSIC_RES_ID", R.raw.cinematic_music)
        startService(intent)


        imageView = findViewById(R.id.imageView)
        slideTextView = findViewById(R.id.slideTextView)
        buttonNext = findViewById(R.id.buttonNext)

        // Initialize the first slide
        updateSlide()

        // Disable the button initially
        buttonNext.isEnabled = false

        // Set up the image change on screen tap
        imageView.setOnClickListener {
            showNextSlide()
        }

        buttonNext.isVisible = false

        // Set up the button action
        buttonNext.setOnClickListener {
            // Navigate to MainActivity
            //val intent = Intent(this, MainActivity::class.java)
            //startActivity(intent)

            openActivity(GameActivity::class)

            finish()
        }
    }

    private fun updateSlide() {
        // Update the image and text for the current slide
        imageView.setImageResource(images[currentIndex])
        applyZoomOutEffect(imageView)

        //slideTextView.text = slideTexts[currentIndex]
        animateText(slideTexts[currentIndex])


    }

    private fun applyZoomOutEffect(imageView: ImageView) {
        val scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", 1.2f, 1f) // De 120% à 100%
        val scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 1.2f, 1f)

        scaleX.duration = 30000 // Durée en ms
        scaleY.duration = 30000

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY)
        animatorSet.interpolator = LinearInterpolator() // Animation linéaire
        animatorSet.start()
    }

    private fun animateText(fullText: String) {
        val delay: Long = 50 // Délai en millisecondes entre chaque lettre
        slideTextView.text = "" // Efface le texte actuel
        var index = 0

        val soundPlayer = SoundPlayer(this)
        soundPlayer.playLoopingSound(R.raw.writter)

        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                if (index < fullText.length) {
                    slideTextView.text = slideTextView.text.toString() + fullText[index]
                    index++
                    handler.postDelayed(this, delay)
                }else{
                    soundPlayer.releasePlayer()
                    soundPlayer.stop()

                    if (currentIndex == images.size - 1) {
                        buttonNext.isEnabled = true
                        buttonNext.isVisible = true
                    }

                }
            }
        }

        handler.post(runnable)
    }




    private fun showNextSlide() {
        if (currentIndex < images.size - 1) {
            currentIndex++
            updateSlide()
        }
    }
}
