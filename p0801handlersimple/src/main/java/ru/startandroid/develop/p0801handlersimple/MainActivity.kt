package ru.startandroid.develop.p0801handlersimple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log

const val LOG_TAG = "myLogs"

class MainActivity : AppCompatActivity() {
    private lateinit var mHandler: Handler
    private var gameOn = false
    private var startTime = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //текущее время в момент запуска
        startTime = System.currentTimeMillis()
        mHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)

                /*
                    Блок if позволяет управлять кодом для потока. Сам запуск мы сделаем позже.
                        А в самом блоке мы снова получаем текущее время и сравниваем с самой первым
                        временем, полученным во время запуска. Для удобства вычисления идут в
                        секундах. Результат выводится в лог.
                 */
                if (gameOn) {
                    val seconds = ((System.currentTimeMillis() - startTime)) / 1000
                    Log.d(LOG_TAG, "seconds = $seconds")
                }

                /*
                    Метод sendEmptyMessageDelayed() сообщает системе, что мы хотим повторять код
                        в handleMessage() раз в секунду.
                 */
                mHandler.sendEmptyMessageDelayed(0, 1000)
            }
        }

        /*
            После инициализации и настройки mHandler мы присваиваем значение true переменной
                gameOn, показывая готовность к запуску кода из блока if.
         */
        gameOn = true
        //Последняя строка sendEmptyMessage() запускает поток
        mHandler.sendEmptyMessage(0)
    }
}