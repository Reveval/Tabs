package ru.startandroid.develop.p0971servicebindclient.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

const val LOG_TAG = "myLogs"

class MyService : Service() {
    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "MyService onCreate")
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(LOG_TAG, "MyService onBind")
        return Binder()
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        Log.d(LOG_TAG, "MyService onRebind")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(LOG_TAG, "MyService onUnbind")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, "MyService onDestroy")
    }
}