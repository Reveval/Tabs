package ru.startandroid.develop.p0931servicestop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ru.startandroid.develop.p0931servicestop.services.MyService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickStart(view: View) {
        startService(Intent(this, MyService::class.java).putExtra(
            "time", 7))
        startService(Intent(this, MyService::class.java).putExtra(
            "time", 2))
        startService(Intent(this, MyService::class.java).putExtra(
            "time", 4))
    }
}