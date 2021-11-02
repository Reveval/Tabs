package ru.startandroid.develop.p0931servicemetanit

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import java.lang.UnsupportedOperationException

class MediaService : Service() {
    lateinit var ambientMediaPlayer: MediaPlayer

    //не имеет реализации
    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    //В методе onCreate() инициализируется медиа-проигрыватель с помощью музыкального ресурса, который добавлен в папку res/raw
    override fun onCreate() {
        ambientMediaPlayer = MediaPlayer.create(this, R.raw.music)
        ambientMediaPlayer.isLooping = true
    }

    //В методе onStartCommand() начинается воспроизведение.
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        ambientMediaPlayer.start()
        return START_STICKY
    }

    override fun onDestroy() {
        ambientMediaPlayer.stop()
    }
}