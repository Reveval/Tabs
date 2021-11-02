package ru.startandroid.develop.p0942servicekillserver

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.concurrent.TimeUnit

const val LOG_TAG = "myLogs"

class MyService : Service() {
    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, "onDestroy")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(LOG_TAG, "onStartCommand")
        readFlags(flags)
        val myRun = MyRun(startId)
        Thread(myRun).start()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun readFlags(flags: Int) {
        when(flags) {
            START_FLAG_REDELIVERY -> Log.d(LOG_TAG, "START_FLAG_REDELIVERY")
            START_FLAG_RETRY -> Log.d(LOG_TAG, "START_FLAG_RETRY")
            0 -> Log.d(LOG_TAG, "readFlags: 0")
            else -> Log.d(LOG_TAG, "readFlags: Error. Unexpected flag returned: $flags")
        }
    }

    inner class MyRun(_startId: Int) : Runnable {
        private val startId = _startId

        init {
            Log.d(LOG_TAG, "MyRun#$startId create")
        }

        override fun run() {
            Log.d(LOG_TAG, "MyRun$startId start")
            try {
                TimeUnit.SECONDS.sleep(15)
            } catch (ex: InterruptedException) {
                ex.printStackTrace()
            }
            stop()
        }

        private fun stop() {
            Log.d(LOG_TAG, "MyRun$startId end, stopSelfResult($startId) = " +
                    "${stopSelfResult(startId)}")
        }
    }
}