package ru.startandroid.develop.p0931servicemetanit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun click(view: View) {
        val intent = Intent(this, MediaService::class.java)
        if (view.id == R.id.start) startService(intent) else stopService(intent)
    }
}