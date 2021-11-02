package ru.startandroid.develop.p0801handlerdemoprogress

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.ProgressBar

class MainActivity : AppCompatActivity() {
    lateinit var progressBar: ProgressBar
    var progressCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressBarHorizontal)
        Thread(myThread).start()
    }

    private val myThread = Runnable {
        val myHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                progressCount++
                progressBar.progress = progressCount
            }
        }

        while (progressCount < 100) {
            try {
                myHandler.sendMessage(myHandler.obtainMessage())
                Thread.sleep(1000)
            } catch (ex: Throwable) {}
        }
    }
}