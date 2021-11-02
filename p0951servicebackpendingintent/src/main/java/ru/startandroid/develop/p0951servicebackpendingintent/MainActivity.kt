package ru.startandroid.develop.p0951servicebackpendingintent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts.*
import ru.startandroid.develop.p0951servicebackpendingintent.services.MyService

class MainActivity : AppCompatActivity() {
    lateinit var textViewTask1: TextView
    lateinit var textViewTask2: TextView
    lateinit var textViewTask3: TextView

    /*
        В onCreate мы находим TextView и присваиваем им первоначальный текст.
            Для каждой задачи свой TextView.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewTask1 = findViewById(R.id.text_view_task1)
        textViewTask1.text = getString(R.string.Task1)
        textViewTask2 = findViewById(R.id.text_view_task2)
        textViewTask2.text = getString(R.string.Task2)
        textViewTask3 = findViewById(R.id.text_view_task3)
        textViewTask3.text = getString(R.string.Task3)
    }

    /*
        В onClickStart мы создаем PendingIntent методом createPendingResult. На вход методу
            передаем только код запроса – можно считать это идентификатором. По этому коду мы потом
            будем определять, какая именно задача вернула ответ из сервиса. Два остальных
            параметра – это Intent и флаги. Нам они сейчас не нужны, передаем соответственно пустой
            Intent и 0. Далее создаем Intent для вызова сервиса MyService, помещаем туда параметр
            времени (который будем использовать для паузы в сервисе) и PendingIntent. После чего,
            отправляем это все в сервис. Аналогичные действия производим для Task2 и Task3.
     */
    fun onClickStart(view: View) {
        //Создаем PendingIntent для Task1
        var pendingIntent = createPendingResult(TASK1_CODE, Intent(), 0)
        //создаем Intent для вызова сервиса, кладем туда параметр времени и созданный PendingIntent
        var intent = Intent(this, MyService::class.java).putExtra(PARAM_TIME, 7)
            .putExtra(PARAM_PINTENT, pendingIntent)
        //стартуем сервис
        startService(intent)

        pendingIntent = createPendingResult(TASK2_CODE, Intent(), 0)
        intent = Intent(this, MyService::class.java).putExtra(PARAM_TIME, 4)
            .putExtra(PARAM_PINTENT, pendingIntent)
        startService(intent)

        pendingIntent = createPendingResult(TASK3_CODE, Intent(), 0)
        intent = Intent(this, MyService::class.java).putExtra(PARAM_TIME, 6)
            .putExtra(PARAM_PINTENT, pendingIntent)
        startService(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(LOG_TAG, "requestCode = $requestCode, resultCode = $resultCode")

        //ловим сообщения о старте задач
        if (resultCode == STATUS_START) {
            when(requestCode) {
                TASK1_CODE -> textViewTask1.text = "Task1 start"
                TASK2_CODE -> textViewTask2.text = "Task2 start"
                TASK3_CODE -> textViewTask3.text = "Task3 start"
            }
        }

        if (resultCode == STATUS_FINISH) {
            val result = data?.getIntExtra(PARAM_RESULT, 0)
            when(requestCode) {
                TASK1_CODE -> textViewTask1.text = "Task1 finish, result = $result"
                TASK2_CODE -> textViewTask2.text = "Task2 finish, result = $result"
                TASK3_CODE -> textViewTask3.text = "Task3 finish, result = $result"
            }
        }
    }

    companion object {
        const val LOG_TAG = "myLogs"

        const val TASK1_CODE = 1
        const val TASK2_CODE = 2
        const val TASK3_CODE = 3

        const val STATUS_START = 100
        const val STATUS_FINISH = 200

        const val PARAM_TIME = "time"
        const val PARAM_PINTENT = "pendingIntent"
        const val PARAM_RESULT = "result"
    }
}