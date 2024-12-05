package com.esilv.hiphiphip.levels

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.esilv.hiphiphip.R
import com.esilv.hiphiphip.data.models.SoundPlayer

class LevelOne : Level() {
    override fun getWordStyle(textView: TextView) {
        textView.textSize = (30..50).random().toFloat()
        textView.rotation = (-30..30).random().toFloat()

        //textView.textSize = 25f
        textView.typeface = ResourcesCompat.getFont(textView.context, R.font.ai)

        val color = ContextCompat.getColor(textView.context, R.color.black)
        textView.setTextColor(color)
    }


    override fun animateWord(view: TextView) {
        val animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        val animatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1f)
        val animatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1f)

        animatorAlpha.duration = 1000
        animatorScaleX.duration = 500
        animatorScaleY.duration = 500

        animatorAlpha.start()
        animatorScaleX.start()
        animatorScaleY.start()
    }


    override fun spawnWordBehaviour(): Pair<Int, Int> {
        return Pair((Math.random() * 500).toInt(), (Math.random() * 800).toInt())
    }

    fun onWordClickEffect2(view: TextView, isCorrect: Boolean) {

        if(isCorrect) {
            // Animation au clic : effet de rebond et disparition
            val animatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.5f, 1f)
            val animatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.5f, 1f)
            val animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)

            val animatorSet = AnimatorSet()
            animatorSet.playTogether(animatorScaleX, animatorScaleY, animatorAlpha)
            animatorSet.duration = 500
            animatorSet.start()
        }else{



            val shake = ObjectAnimator.ofFloat(view, "translationX", 0f, 25f, -25f, 15f, -15f, 5f, -5f, 0f)
            shake.duration = 500

            shake.start()

        }
    }

    override fun onWordClickEffect(view: TextView, isCorrect: Boolean) {

        val soundPlayer = SoundPlayer(view.context)
        soundPlayer.playSoundWhenClickingWord(isCorrect)

        if (isCorrect) {
            // Animation au clic : effet de rebond et disparition
            view.isClickable = false
            view.animate()
                .scaleX(1.5f)
                .scaleY(1.5f)
                .alpha(0f)
                .setDuration(500)
                .start()
        } else {
            // Animation simple de rotation pour simuler un "non"
            view.animate()
                .rotationBy(20f) // Rotation dans un sens
                .setDuration(100)
                .withEndAction {
                    view.animate()
                        .rotationBy(-40f) // Rotation dans l'autre sens
                        .setDuration(100)
                        .withEndAction {
                            view.animate()
                                .rotationBy(20f) // Revenir à la position initiale
                                .setDuration(100)
                                .start()
                        }
                }
                .start()
        }
    }

}