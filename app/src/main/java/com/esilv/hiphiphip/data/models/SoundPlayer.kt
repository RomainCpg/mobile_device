package com.esilv.hiphiphip.data.models

import android.content.Context
import android.media.MediaPlayer
import com.esilv.hiphiphip.R

class SoundPlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun playSoundWhenClickingWord(isCorrect: Boolean) {

        // Libère l'ancien MediaPlayer s'il existe
        mediaPlayer?.release()

        // Initialise et joue le son

        if(isCorrect){
            mediaPlayer = MediaPlayer.create(context, R.raw.success)
        }else{
            mediaPlayer = MediaPlayer.create(context, R.raw.fail)
        }

        mediaPlayer?.setOnCompletionListener {
            it.release() // Libère les ressources une fois le son terminé
            mediaPlayer = null
        }
        mediaPlayer?.start()
    }

    fun playLoopingSound(sound: Int) {

        // Libère l'ancien MediaPlayer s'il existe
        mediaPlayer?.release()

        // Initialise et joue le son

        mediaPlayer = MediaPlayer.create(context, sound)
        mediaPlayer?.isLooping = true


        mediaPlayer?.start()
    }

    fun releasePlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun stop(){
        mediaPlayer?.stop()
    }
}
