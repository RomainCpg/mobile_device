package com.esilv.hiphiphip.services
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.esilv.hiphiphip.R
class BackgroundMusicService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private var musicResId: Int = R.raw.game_background // Musique par défaut

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Récupère l'identifiant de la ressource musicale depuis l'Intent
        intent?.getIntExtra("MUSIC_RES_ID", R.raw.game_background)?.let {
            musicResId = it
        }

        // Initialise et démarre le lecteur
        mediaPlayer?.stop() // Arrête le lecteur existant s'il existe
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, musicResId).apply {
            isLooping = true // Musique en boucle
            setVolume(0.5f, 0.5f)
            start()
        }

        return START_STICKY // Redémarre automatiquement en cas d'arrêt du système
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
