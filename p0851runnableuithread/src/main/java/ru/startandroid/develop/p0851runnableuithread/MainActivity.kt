package ru.startandroid.develop.p0851runnableuithread

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import java.util.concurrent.TimeUnit

const val LOG_TAG = "myLogs"

class MainActivity : AppCompatActivity() {
    private lateinit var textViewInfo: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewInfo = findViewById(R.id.text_view_info)
        val thread = Thread {
            try {
                TimeUnit.SECONDS.sleep(2)
                runOnUiThread(runn1)
                TimeUnit.SECONDS.sleep(1)
                textViewInfo.postDelayed(runn3, 2000)
                textViewInfo.post(runn2)
            } catch (ex: InterruptedException) {
                ex.printStackTrace()
            }
        }
        thread.start()
    }

    @SuppressLint("SetTextI18n")
    val runn1 = Runnable {
        textViewInfo.text = "runn1"
    }

    @SuppressLint("SetTextI18n")
    val runn2 = Runnable {
        textViewInfo.text = "runn2"
    }

    @SuppressLint("SetTextI18n")
    val runn3 = Runnable {
        textViewInfo.text = "runn3"
    }
}