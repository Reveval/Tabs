package ru.startandroid.develop.p0921servicesimple

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ru.startandroid.develop.p0921servicesimple.services.MyService

const val LOG_TAG = "myLogs"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /*
        Здесь у нас два метода, которые срабатывают при нажатии на кнопки Start service и
            Stop service. В них мы соответственно запускаем или останавливаем сервис методами
            startService и stopService. На вход передаем Intent, указывающий на сервис. Это очень
            похоже на то, как мы вызываем Activity методом startActivity.
     */
    fun onClickStart(view: View) {
        startService(Intent(this, MyService::class.java))
    }

    fun onClickStop(view: View) {
        stopService(Intent(this, MyService::class.java))
    }
}