package ru.startandroid.develop.p0951servicebackpendingintent.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import ru.startandroid.develop.p0951servicebackpendingintent.MainActivity
import java.util.concurrent.Executor
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
        Log.d(LOG_TAG, "onStartCommand")
        val time = intent?.getIntExtra(MainActivity.PARAM_TIME, 1)
        val pi = intent?.getParcelableExtra<PendingIntent>(MainActivity.PARAM_PINTENT)

        val myRun = MyRun(time, startId, pi)
        es.execute(myRun)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    /*
        MyRun при запуске вызывает метод send для PendingIntent и передает туда тип сообщения
            STATUS_START. Это приходит в метод onActivityResult в Activity и на экране мы увидим,
            как в одном из TextView появится текст, что задача начала работать. Далее мы эмулируем
            работу, как обычно, просто поставив паузу. А после этого создаем Intent с результатом
            работы (просто время * 100), и вызываем немного другую реализацию метода send. Кроме
            типа сообщения (STATUS_FINISH), мы передаем туда Intent с результатом и указываем
            контекст. Это идет в метод onActivityResult в Activity и на экране мы увидим, как в
            одном из TextView появится текст, что задача закончила работу с неким результатом.
     */
    inner class MyRun(_time: Int?, _startId: Int, _pi: PendingIntent?) : Runnable {
        private val time = _time
        private val startId = _startId
        private val pi = _pi

        init {
            Log.d(LOG_TAG, "MyRun#$startId create")
        }

        override fun run() {
            Log.d(LOG_TAG, "MyRun$startId start, time = $time")
            try {
                //сообщаем о старте задачи
                pi?.send(MainActivity.STATUS_START)
                //Начинаем выполнение задачи
                if (time != null) {
                    TimeUnit.SECONDS.sleep(time.toLong())
                }
                //сообщаем о окончании задачи
                val intent = Intent().putExtra(MainActivity.PARAM_RESULT, time?.times(100))
                pi?.send(this@MyService, MainActivity.STATUS_FINISH, intent)
            } catch (ex: InterruptedException) {
                ex.printStackTrace()
            } catch (ex: PendingIntent.CanceledException) {
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