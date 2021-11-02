package ru.startandroid.develop.p0981servicebindinglocal.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.util.*
import kotlin.concurrent.timerTask

const val LOG_TAG = "myLogs"

class MyService : Service() {
    private val binder = MyBinder()
    private var interval = 1000L

    /*
        Здесь мы используем таймер – Timer. Он позволяет повторять какое-либо действие через
            заданный промежуток времени.
     */
    private lateinit var timer: Timer
    //TimerTask – это задача, которую Timer будет периодически выполнять.
    private lateinit var timerTask: TimerTask

    /*
        Итак, в методе onCreate мы создаем таймер и выполняем метод schedule, в котором
            стартует задача.
     */
    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "MyService onCreate")
        timer = Timer()
        schedule()
    }

    /*
        Метод schedule проверяет, что задача уже создана и отменяет ее. Далее планирует новую,
            с отложенным на 1000 мс запуском и периодом = interval. Т.е. можно сказать, что этот
            метод перезапускает задачу с использованием текущего интервала повтора (interval),
            а если задача еще не создана, то создает ее. Сама задача просто выводит в лог текст
            run. Если interval = 0, то ничего не делаем.
     */
    private fun schedule() {
        if (::timerTask.isInitialized) timerTask.cancel()
        if (interval > 0) {
            timerTask = timerTask {
                Log.d(LOG_TAG, "run")
            }
            timer.schedule(timerTask, 1000, interval)
        }
    }

    /*
        Метод upInterval получает на вход значение, увеличивает interval на это значение и
            перезапускает задачу. Соответственно задача после этого будет повторяться реже.
     */
    fun upInterval(gap: Long) : Long {
        interval += gap
        schedule()
        return interval
    }

    /*
        Метод downInterval получает на вход значение, уменьшает interval на это значение
            (но так, чтоб не меньше 0) и перезапускает задачу. Соответственно задача после этого
            будет повторяться чаще.
     */
    fun downInterval(gap: Long) : Long {
        interval -= gap
        schedule()
        return interval
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(LOG_TAG, "MyService onBind")
        return binder
    }

    inner class MyBinder : Binder() {
        val service
            get() = this@MyService
    }
}