package ru.startandroid.develop.p0801handlerex2

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.util.concurrent.TimeUnit

const val LOG_TAG = "myLogs"

class MainActivity : AppCompatActivity() {
    lateinit var handler: Handler
    private lateinit var textViewInfo: TextView
    lateinit var buttonStart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewInfo = findViewById(R.id.text_view_info)
        buttonStart = findViewById(R.id.button_start)
        handler = Handler()
    }

    @SuppressLint("SetTextI18n")
    fun onclick(view: View) {
        when(view.id) {
            R.id.button_start -> {
                val thread = Thread {
                    for (i in 1..10) {
                        //долгий процесс
                        downloadFile()
                        //обновляем textView
                        val message = "Закачано файлов: $i"
                        textViewInfo.text = message
                        //пишем лог
                        Log.d(LOG_TAG, message)
                    }
                }
                thread.start()
            }
            R.id.button_test -> Log.d(LOG_TAG, "test")
        }
    }

    //downloadFile – эмулирует закачку файла, это просто пауза в одну секунду.
    private fun downloadFile() {
        try {
            TimeUnit.SECONDS.sleep(1)
        } catch (ex: InterruptedException) {
            ex.printStackTrace()
        }
    }
}