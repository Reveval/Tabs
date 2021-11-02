package ru.startandroid.develop.p0941servicekillclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ru.startandroid.develop.p0941servicekillclient.service.MyService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickStart(view: View) {
        startService(Intent(this,
            MyService::class.java).putExtra("name", "value"))
    }
}