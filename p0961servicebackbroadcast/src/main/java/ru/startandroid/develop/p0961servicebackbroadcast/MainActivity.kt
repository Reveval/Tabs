package ru.startandroid.develop.p0961servicebackbroadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import ru.startandroid.develop.p0961servicebackbroadcast.services.MyService

class MainActivity : AppCompatActivity() {
    lateinit var textViewTask1: TextView
    lateinit var textViewTask2: TextView
    lateinit var textViewTask3: TextView

    lateinit var br: BroadcastReceiver

    /*
        В onCreate находим TextView и присваиваем им начальные тексты. Далее создаем
            BroadcastReceiver и реализуем в нем метод onReceive. Все Intent-ы, которые получит
            BroadcastReceiver, будут переданы в этот метод нам на обработку. Мы извлекаем из
            Intent-а данные о задаче (код и статус) и меняем информацию о ней в соответствующем
            TextView. Если пришел статус STATUS_START – задача начала работу. Если STATUS_FINISH –
            закончила работу и Intent должен содержать результат (PARAM_RESULT).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewTask1 = findViewById(R.id.tvTask1)
        textViewTask1.text = "Task1"

        textViewTask2 = findViewById(R.id.tvTask2)
        textViewTask2.text = "Task2"

        textViewTask3 = findViewById(R.id.tvTask3)
        textViewTask3.text = "Task3"

        //создаем BroadcastReceiver
        br = object : BroadcastReceiver() {
            //действия при получении сообщений
            override fun onReceive(context: Context?, intent: Intent?) {
                val task = intent?.getIntExtra(PARAM_TASK, 0)
                val status = intent?.getIntExtra(PARAM_STATUS, 0)
                Log.d(LOG_TAG, "onReceive: task = $task, status = $status")

                //Ловим сообщения о старте задач
                if (status == STATUS_START) {
                    when(task) {
                        TASK1_CODE -> textViewTask1.text = "Task1 start"
                        TASK2_CODE -> textViewTask2.text = "Task2 start"
                        TASK3_CODE -> textViewTask3.text = "Task3 start"
                    }
                }

                //Ловим сообщения о окончании задач
                if (status == STATUS_FINISH) {
                    val result = intent.getIntExtra(PARAM_RESULT, 0)
                    when(task) {
                        TASK1_CODE -> textViewTask1.text = "Task 1 finish, result = $result"
                        TASK2_CODE -> textViewTask2.text = "Task 2 finish, result = $result"
                        TASK3_CODE -> textViewTask3.text = "Task 3 finish, result = $result"
                    }
                }
            }
        }
        //создаем фильтр для BroadcastReceiver
        val intentFilter = IntentFilter(BROADCAST_ACTION)
        //регистрируем(включаем) BroadcastReceiver
        registerReceiver(br, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        //дерегистрируем(выключаем) BroadcastReceiver
        unregisterReceiver(br)
    }

    fun onClickStart(view: View) {
        //Созадем intent для вызова сервиса, кладем туда параметр времени и код задачи
        var intent = Intent(this, MyService::class.java)
            .putExtra(PARAM_TIME, 7).putExtra(PARAM_TASK, TASK1_CODE)
        //стартуем сервис
        startService(intent)

        intent = Intent(this, MyService::class.java)
            .putExtra(PARAM_TIME, 4).putExtra(PARAM_TASK, TASK2_CODE)
        startService(intent)

        intent = Intent(this, MyService::class.java)
            .putExtra(PARAM_TIME, 6).putExtra(PARAM_TASK, TASK3_CODE)
        startService(intent)
    }

    companion object {
        const val LOG_TAG = "myLogs"

        const val TASK1_CODE = 1
        const val TASK2_CODE = 2
        const val TASK3_CODE = 3

        const val STATUS_START = 100
        const val STATUS_FINISH = 200

        const val PARAM_TIME = "time"
        const val PARAM_TASK = "task"
        const val PARAM_RESULT = "result"
        const val PARAM_STATUS = "status"

        const val BROADCAST_ACTION = "ru.startandroid.develop.p0961servicebackbroadcast"
    }
}