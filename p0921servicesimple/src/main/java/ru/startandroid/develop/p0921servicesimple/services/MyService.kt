package ru.startandroid.develop.p0921servicesimple.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.concurrent.TimeUnit

class MyService : Service() {
    //срабатывает при создании сервиса
    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, MESSAGE_ON_CREATE)
    }

    //Метод onStartCommand – срабатывает, когда сервис запущен методом startService.
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(LOG_TAG, MESSAGE_ON_START_COMMAND)
        someTask()
        return super.onStartCommand(intent, flags, startId)
    }

    //вызывается при уничтожении сервиса
    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, MESSAGE_ON_DESTROY)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(LOG_TAG, MESSAGE_ON_BIND)
        return null
    }

    //someTask – здесь будем кодить работу для сервиса
    private fun someTask() {
        Thread {
            for (i in 1..5) {
                Log.d(LOG_TAG, "i = $i")
                try {
                    TimeUnit.SECONDS.sleep(1)
                } catch (ex: InterruptedException) {
                    ex.printStackTrace()
                }
            }
            /*
                stopSelf – этот метод аналогичен методу stopService, он останавливает сервис,
                    в котором был вызван.
             */
            stopSelf()
        }.start()
    }

    companion object {
        const val LOG_TAG = "myLogs"
        const val MESSAGE_ON_CREATE = "onCreate"
        const val MESSAGE_ON_START_COMMAND = "onStartCommand"
        const val MESSAGE_ON_DESTROY = "onDestroy"
        const val MESSAGE_ON_BIND = "onBind"
    }
}