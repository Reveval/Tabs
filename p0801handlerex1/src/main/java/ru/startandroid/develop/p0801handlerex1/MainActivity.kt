package ru.startandroid.develop.p0801handlerex1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    //считаем нажатия кнопки
    private var mButtonPressed = 0
    //счетчик времени
    var mTime = 0L
    //обработчик потока - обновляет сведения о времени
    val mHandler: Handler = Handler(Looper.getMainLooper())

    lateinit var textViewTime: TextView
    private lateinit var textViewCounter: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewTime = findViewById(R.id.text_view_time)
        textViewCounter = findViewById(R.id.text_view_counter)

        if (mTime == 0L) {
            mTime = SystemClock.uptimeMillis()
            mHandler.removeCallbacks(timeUpdaterRunnable)
            /*
                Добавляем Runnable-объект timeUpdaterRunnable в очередь сообщений, объект должен
                    быть запущен после задержки в 100 мс
             */
            mHandler.postDelayed(timeUpdaterRunnable, 100)
        }
    }

    //Описание Runnable-объекта
    @SuppressLint("SetTextI18n")
    private val timeUpdaterRunnable = object : Runnable {
        override fun run() {
            val start = mTime
            val millis = SystemClock.uptimeMillis() - start
            var second = (millis / 1000).toInt()
            val min = second / 60
            second %= 60
            //выводим время
            textViewTime.text = "$min:${String.format("%02d", second)}"
            //повторяем через каждые 200 мс
            mHandler.postDelayed(this, 200)
        }
    }

    override fun onPause() {
        //удаляем Runnable-объект для прекращения задачи
        mHandler.removeCallbacks(timeUpdaterRunnable)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        //добавляем Runnable-объект
        mHandler.postDelayed(timeUpdaterRunnable, 100)
    }

    fun onClick(view: View) {
        textViewCounter.text = "${++mButtonPressed}"
    }
}