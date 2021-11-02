package ru.startandroid.develop.p0971servicebindclient

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import ru.startandroid.develop.p0971servicebindclient.service.MyService

const val LOG_TAG = "myLogs"
const val ACTION = "ru.startandroid.develop.p0972servicebindserver.MyService"

class MainActivity : AppCompatActivity() {
    var bound = false
    lateinit var serviceConnection: ServiceConnection
    lateinit var myIntent: Intent

    //В onCreate мы создаем Intent, который позволит нам добраться до сервиса.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myIntent = Intent(this, MyService::class.java)

        /*
            Объект ServiceConnection позволит нам определить, когда мы подключились к сервису и
                когда связь с сервисом потеряна (если сервис был убит системой при нехватке
                памяти). При подключении к сервису сработает метод onServiceConnected. На вход он
                получает имя компонента-сервиса и объект Binder для взаимодействия с сервисом.
                В этом уроке мы этим Binder пока не пользуемся. При потере связи сработает метод
                onServiceDisconnected.
                Переменную bound мы используем для того, чтобы знать – подключены мы в данный
                момент к сервису или нет. Соответственно при подключении мы переводим ее в true,
                а при потере связи в false.
         */
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                Log.d(LOG_TAG, "MainActivity onServiceConnected")
                bound = true
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                Log.d(LOG_TAG, "MainActivity onServiceDisconnected")
                bound = false
            }
        }
    }

    //стартуем сервис
    fun onClickStart(view: View) {
        startService(myIntent)
    }
    //останавливаем сервис
    fun onClickStop(view: View) {
        stopService(myIntent)
    }

    /*
        В onClickBind – соединяемся с сервисом, используя метод bindService. На вход передаем
            Intent, ServiceConnection и флаг BIND_AUTO_CREATE, означающий, что, если сервис, к
            которому мы пытаемся подключиться, не работает, то он будет запущен.
     */
    fun onClickBind(view: View) {
        bindService(myIntent, serviceConnection, BIND_AUTO_CREATE)
    }

    /*
        В onClickUnBind с помощью bound проверяем, что соединение уже установлено. Далее
            отсоединяемся методом unbindService, на вход передавая ему ServiceConnection. И в
            bound пишем false, т.к. мы сами разорвали соединение. Метод onServiceDisconnected не
            сработает при явном отключении.
     */
    fun onClickUnBind(view: View?) {
        if (!bound) return
        unbindService(serviceConnection)
        bound = false
    }

    override fun onDestroy() {
        super.onDestroy()
        onClickUnBind(null)
    }
}