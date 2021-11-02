package ru.startandroid.develop.p0931servicestop.services

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MyService : Service() {
    lateinit var es: ExecutorService
    private var someRes: Any? = null

    /*
        В onCreate создаем некий объект someRes. На примере этого объекта покажем, какую нехорошую
            ситуацию с ресурсами можно получить с применением метода stopSelf(startId). Этот объект
            будет использоваться сервисом в обработках вызовов.
     */
    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, MESSAGE_ON_CREATE)
        /*
            Executors.newFixedThreadPool(1) – дает нам объект-экзекьютор, который будет получать от
                нас задачи (Runnable) и запускать их по очереди в одном потоке (на вход ему мы
                передаем значение 1). Он сделает за нас всю работу по управлению потоками.
         */
        es = Executors.newFixedThreadPool(3)
        someRes = Any()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, MESSAGE_ON_DESTROY)
        someRes = null
    }

    /*
        Метод onStartCommand принимает аргументы:
            Первый – это Intent.  Тот самый, который отправляется в путь, когда мы стартуем сервис
            с помощью метода startService. Соответственно вы можете использовать его для передачи
            данных в ваш сервис. Тут все аналогично, как при вызове другого Activity – там вы
            тоже можете передать данные с помощью intent.
            Второй параметр – флаги запуска. Он нам пока не нужен, пропускаем его.
            Третий параметр – startId. Простыми словами – это счетчик вызовов startService
            пока сервис запущен.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(LOG_TAG, MESSAGE_ON_START_COMMAND)
        val time = intent?.getIntExtra("time", 1)
        val myRun = MyRun(time, startId)
        es.execute(myRun)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    /*
        MyRun – Runnable-объект. Он и будет обрабатывать входящие вызовы сервиса. В конструкторе
            он получает time и startId. Параметр time будет использован для кол-ва секунд паузы
            (т.е. эмуляции работы). А startId будет использован в методе stopSelf(startId), который
            даст сервису понять, что вызов под номером startId обработан. В лог выводим инфу о
            создании, старте и завершении работы. Также здесь используем объект someRes, в лог
            просто выводим его класс. Если же объект = null, то ловим эту ошибку и выводим ее в лог.
     */
    inner class MyRun(time: Int?, startId: Int) : Runnable {
        private val timeRun = time
        private val startIdRun = startId

        init {
            Log.d(LOG_TAG, "MyRun#$startIdRun create")
        }

        override fun run() {
            Log.d(LOG_TAG, "MyRun#$startIdRun start, time = $timeRun")
            try {
                TimeUnit.SECONDS.sleep(timeRun!!.toLong())
            } catch (ex: InterruptedException) {
                ex.printStackTrace()
            }
            try {
                Log.d(LOG_TAG, "MyRun#$startIdRun someRes = ${someRes!!.javaClass}")
            } catch (ex: NullPointerException) {
                Log.d(LOG_TAG, "MyRun$startIdRun error, null pointer")
            }
            stop()
        }

        private fun stop() {
            Log.d(LOG_TAG, "MyRun#$startIdRun end, stopSelf(\"$startIdRun\")")
            /*
                Механизм работы данного метода может иметь последствия, т.к. этот метод
                    останавливает сервис, когда последний полученный вызов выполняет этот метод.
                    Даже если остались еще работающие вызовы.
             */
            stopSelf(startIdRun)
        }
    }

    companion object {
        const val LOG_TAG = "myLogs"
        const val MESSAGE_ON_CREATE = "MyService onCreate"
        const val MESSAGE_ON_START_COMMAND = "MyService onStartCommand"
        const val MESSAGE_ON_DESTROY = "MyService onDestroy"
    }
}