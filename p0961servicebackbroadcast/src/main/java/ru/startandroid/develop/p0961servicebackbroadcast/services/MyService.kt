package ru.startandroid.develop.p0961servicebackbroadcast.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import ru.startandroid.develop.p0961servicebackbroadcast.MainActivity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

const val LOG_TAG = "myLogs"

class MyService : Service() {
    lateinit var es: ExecutorService

    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "MyService onCreate")
        es = Executors.newFixedThreadPool(2)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, "MyService onDestroy")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(LOG_TAG, "MyService onStartCommand")

        val time = intent?.getIntExtra(MainActivity.PARAM_TIME, 1)
        val task = intent?.getIntExtra(MainActivity.PARAM_TASK, 0)

        val myRun = MyRun(startId, time, task)
        es.execute(myRun)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    inner class MyRun(_startId: Int, _time: Int?, _task: Int?) : Runnable {
        private val startId = _startId
        private val time = _time
        private val task = _task

        init {
            Log.d(LOG_TAG, "MyRun#$startId create")
        }

        override fun run() {
            val intent = Intent(MainActivity.BROADCAST_ACTION)
            Log.d(LOG_TAG, "MyRun$startId start, time = $time")
            try {
                //сообщаем о старте задачи
                intent.putExtra(MainActivity.PARAM_TASK, task)
                intent.putExtra(MainActivity.PARAM_STATUS, MainActivity.STATUS_START)
                sendBroadcast(intent)

                //начинаем выполнение задачи
                if (time != null) {
                    TimeUnit.SECONDS.sleep(time.toLong())
                }

                //сообщаем о окончании задачи
                intent.putExtra(MainActivity.PARAM_STATUS, MainActivity.STATUS_FINISH)
                intent.putExtra(MainActivity.PARAM_RESULT, time?.times(100))
                sendBroadcast(intent)
            } catch (ex: InterruptedException) {
                ex.printStackTrace()
            }
            stop()
        }

        private fun stop() {
            Log.d(LOG_TAG, "MyRun#$startId end, stopSelfResult($startId) = " +
                    "${stopSelfResult(startId)}")
        }
    }
}