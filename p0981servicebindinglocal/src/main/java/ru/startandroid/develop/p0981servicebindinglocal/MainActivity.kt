package ru.startandroid.develop.p0981servicebindinglocal

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.TextView
import ru.startandroid.develop.p0981servicebindinglocal.services.MyService

const val LOG_TAG = "myLogs"

class MainActivity : AppCompatActivity() {
    var bound = false
    var interval = 0L
    lateinit var sc: ServiceConnection
    lateinit var myIntent: Intent
    lateinit var myService: MyService
    lateinit var tvInterval: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvInterval = findViewById(R.id.tvInterval)
        myIntent = Intent(this, MyService::class.java)
        sc = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                Log.d(LOG_TAG, "MainActivity onServiceConnected")
                Log.d(LOG_TAG, "javaClass: ${MyService.MyBinder::class.java}")
                myService = (service as MyService.MyBinder).service
                bound = true
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                Log.d(LOG_TAG, "MainActivity onServiceDisconnected")
                bound = false
            }
        }
    }

    //подключаемся к сервису
    override fun onStart() {
        super.onStart()
        bindService(myIntent, sc, 0)
    }

    //останавливаем сервис
    override fun onStop() {
        super.onStop()
        if (!bound) return
        unbindService(sc)
        bound = false
    }

    //В onClickStart запускаем сервис.
    fun onClickStart(view: View) {
        startService(myIntent)
    }

    fun onClickUp(view: View) {
        if (!bound) return
        interval = myService.upInterval(500)
        tvInterval.text = "interval = $interval"
    }

    fun onClickDown(view: View) {
        if (!bound) return
        interval = myService.downInterval(500)
        tvInterval.text = "interval = $interval"
    }
}