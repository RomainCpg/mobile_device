package com.esilv.hiphiphip.ui.activities

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.esilv.hiphiphip.R
import com.esilv.hiphiphip.levels.Level
import com.esilv.hiphiphip.levels.LevelOne
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.text.TextUtils
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import com.esilv.hiphiphip.services.BackgroundMusicService

class GameActivity : AppCompatActivity() {

    private var score = 0
    private var timeLeft = 60

    private lateinit var scoreTextView: TextView
    private lateinit var timerTextView: TextView
    private lateinit var imageViewBackground: ImageView
    private lateinit var wordsContainer: FrameLayout

    private lateinit var currentLevel: Level

    private val spawnHandler = Handler();
    private var spawnInterval = 2000L;
    private var currentSpawnCount = 0;
    private val maxWords = 50;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Initialize UI elements
        scoreTextView = findViewById(R.id.textViewScore)
        timerTextView = findViewById(R.id.textViewTimer)
        imageViewBackground = findViewById(R.id.imageViewBackground)
        wordsContainer = findViewById(R.id.wordsContainer)

        // Set a random background
        val backgrounds = arrayOf(R.drawable.fond)
        imageViewBackground.setImageResource(backgrounds.random())

        // Set the level
        currentLevel = LevelOne()

        // Start the game timer
        startTimer()

        startWordSpawn()

        //start the music (we need to stop it in an other activity like at the end of the game)
        val intent = Intent(this, BackgroundMusicService::class.java)
        startService(intent)


        // Spawn random words (some correct, some not!)
        //repeat(6) { showRandomWord("houras") }
        //repeat(4) { showRandomWord("random_word_${it}") }
    }

    private val spawnRunnable = object : Runnable {
        override fun run() {
            if (currentSpawnCount < maxWords) {
                // Générer et afficher un mot aléatoire
                val randomWord = getRandomWord()
                showRandomWord(randomWord)

                // Augmenter le compteur
                currentSpawnCount++

                // Réduire progressivement l'intervalle
                if (spawnInterval > 500) {
                    spawnInterval -= 30 // Réduction de l'intervalle
                }

                // Planifier le prochain spawn
                spawnHandler.postDelayed(this, spawnInterval)
            }
        }
    }

    private fun startWordSpawn() {
        spawnHandler.post(spawnRunnable)
    }

    private fun stopWordSpawn() {
        spawnHandler.removeCallbacks(spawnRunnable)
    }

    private fun getRandomWord(): String {
        val wordList = listOf(
            "houras", "h0uraz", "houras", "hoouras", "houras", "hourras",
            "houras", "hooras", "houras", "hohras", "houras", "h0uras",
            "houras", "huoras", "houras", "hurras", "houras", "h0urass",
            "houras", "houraaz", "houras", "hourass", "houras", "hourar",
            "houras", "h0ouras", "houras", "hhooras", "houras", "horuas",
            "houras", "hu0ras", "houras", "huraz", "houras", "hourz",
            "houras", "hhouras", "houras", "apple", "houras", "banana",
            "houras", "grape", "houras", "orange", "houras", "pear",
            "houras", "house", "houras", "river", "houras", "mountain",
            "houras", "cloud", "houras", "tree", "houras", "happy",
            "houras", "sad", "houras", "quick", "houras", "slow",
            "houras", "bright", "houras", "funny", "houras", "serious",
            "houras", "simple", "houras", "complex", "houras", "random",
            "houras", "light", "houras", "dark", "houras", "fire",
            "houras", "water", "houras", "earth", "houras", "storm",
            "houras", "wind", "houras", "snow", "houras", "rain",
            "houras", "sun", "houras", "cat", "houras", "dog",
            "houras", "fish", "houras", "bird", "houras", "lion",
            "houras", "flower", "houras", "grass", "houras", "leaf",
            "houras", "rock", "houras", "sand", "houras", "houras",
            "houras", "houras", "houras", "houras", "houras"
        )


        return wordList.random()
    }


    // Start a countdown timer
    private fun startTimer() {
        val countDownTimer = object : CountDownTimer(timeLeft * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                timerTextView.text = "Time: ${String.format("%02d", secondsRemaining / 60)}:${String.format("%02d", secondsRemaining % 60)}"
            }

            override fun onFinish() {
                startActivity(Intent(this@GameActivity, TimeFinishedActivity::class.java))
                Log.d("timer", "Game over! Time's up!")
            }
        }
        countDownTimer.start()
    }

    // Update the score display
    private fun updateScore() {
        scoreTextView.text = "Score: $score"
    }

    // Show a random word in a random position on the screen
    private fun showRandomWord(word: String) {
        val textView = TextView(this)


        // Définir une largeur fixe pour le TextView (par exemple 300dp)
        val widthInDp = 10000000 // largeur de base en dp
        val heightInDp = 100 // hauteur de base en dp

        // Conversion des dimensions en pixels
        val density = resources.displayMetrics.density
        val widthInPx = (widthInDp * density).toInt()
        val heightInPx = (heightInDp * density).toInt()

        // Appliquer les LayoutParams avec la largeur et hauteur fixes
        val layoutParams = FrameLayout.LayoutParams(widthInPx, heightInPx)
        textView.layoutParams = layoutParams


        // Apply level-specific style and animations
        currentLevel.getWordStyle(textView)
        currentLevel.animateWord(textView)

        textView.text = word
        textView.isClickable = true



        wordsContainer.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Ensure the container is ready
                wordsContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val containerWidth = wordsContainer.width
                val containerHeight = wordsContainer.height

                if (containerWidth > 0 && containerHeight > 0) {
                    val layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT
                    )

                    // Random positioning
                    val minMargin = 0.2 // 20% des bords
                    val maxMarginX = containerWidth * (1 - minMargin)
                    val maxMarginY = containerHeight * (1 - minMargin)

                    layoutParams.leftMargin = (minMargin * containerWidth + Math.random() * maxMarginX).toInt()
                    layoutParams.topMargin = (minMargin * containerHeight + Math.random() * maxMarginY).toInt()

                    layoutParams.leftMargin = maxMarginX.toInt()
                    layoutParams.topMargin = maxMarginY.toInt()

                    textView.layoutParams = layoutParams


                    // Handle word click
                    textView.setOnClickListener {
                        val isCorrect = word == "houras" // Only "houras" is correct
                        if (isCorrect) {
                            score++
                            updateScore()
                        }
                        currentLevel.onWordClickEffect(textView, isCorrect)
                    }

                    wordsContainer.addView(textView)
                    startRandomMovement(textView, containerWidth, containerHeight, layoutParams)
                }
            }
        })
    }

    // Create random movement animation for a word
    private fun startRandomMovement(textView: TextView, containerWidth: Int, containerHeight: Int, layout: FrameLayout.LayoutParams) {
        // Durées aléatoires pour des vitesses variées
        val durationX = (10000..15000).random().toLong()
        val durationY = (8000..15000).random().toLong()

        // Limites pour rester dans les bornes
        val maxTranslationX = (containerWidth - textView.width).coerceAtLeast(0).toFloat()
        val maxTranslationY = (containerHeight - textView.height).coerceAtLeast(0).toFloat()

        // Position actuelle du mot
        val startX = layout.leftMargin.toFloat();
        val startY = layout.topMargin.toFloat();



        // Calculer la direction en fonction de la position actuelle
        val targetX = when {
            startX < containerWidth / 2 -> maxTranslationX // Si à gauche, aller à droite
            else -> -maxTranslationX // Si à droite, aller à gauche
        }

        val targetY = when {
            startY < containerHeight / 2 -> maxTranslationY // Si en haut, aller en bas
            else -> -maxTranslationY // Si en bas, aller en haut
        }

        val animatorX = ObjectAnimator.ofFloat(
            textView,
            "translationX",
            startX,
            targetX
        ).apply {
            duration = durationX
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator() // Mouvement fluide
        }

        val animatorY = ObjectAnimator.ofFloat(
            textView,
            "translationY",
            startY,
            targetY
        ).apply {
            duration = durationY
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator() // Mouvement fluide
        }

        // Démarrage des animations
        animatorX.start()
        animatorY.start()
    }



    // Vibrate the device for feedback
    fun vibrate() {
        val vib = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vib.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vib.vibrate(300)
        }
    }
}
