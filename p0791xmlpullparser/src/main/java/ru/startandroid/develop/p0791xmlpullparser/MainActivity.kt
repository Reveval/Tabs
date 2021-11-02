package ru.startandroid.develop.p0791xmlpullparser

import android.content.res.XmlResourceParser
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import org.xmlpull.v1.XmlPullParser.*
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var temp: String
        var message: String

        /*
            В onCreate мы получаем XmlPullParser с помощью метода prepareXpp и начинаем его
                разбирать. Затем в цикле while мы запускаем прогон документа, пока не достигнем
                конца - END_DOCUMENT. Прогон обеспечивается методом next в конце цикла while.
                В switch мы проверяем на каком элементе остановился парсер.
         */
        try {
            val parser = getParser()
            while (parser.eventType != END_DOCUMENT) {
                when(parser.eventType) {
                    //начало документа
                    START_DOCUMENT -> Log.d(LOG_TAG, MESSAGE_START_DOCUMENT)
                    /*
                        START_TAG – начало тега. Выводим в лог имя тэга, его уровень в дереве тэгов
                            (глубину) и количество атрибутов. Следующей строкой выводим имена и
                            значения атрибутов, если они есть.
                     */
                    START_TAG -> {
                        message = "START_TAG: name = ${parser.name}, depth = ${parser.depth}, " +
                                "attrCount = ${parser.attributeCount}"
                        Log.d(LOG_TAG, message)
                        temp = ""
                        for (i in 0 until parser.attributeCount) {
                            temp += "${parser.getAttributeName(i)} = ${parser.getAttributeValue(i)}, "
                        }
                        if (!TextUtils.isEmpty(temp)) {
                            message = "Attributes: $temp"
                            Log.d(LOG_TAG, message)
                        }
                    }
                    //конец тега
                    END_TAG -> {
                        message = "END_TAG: name = ${parser.name}"
                        Log.d(LOG_TAG, message)
                    }
                    //содержимое тега
                    TEXT -> {
                        message = "text = ${parser.text}"
                        Log.d(LOG_TAG, message)
                    }
                }
                //следующий элемент
                parser.next()
            }
            Log.d(LOG_TAG, MESSAGE_END_DOCUMENT)
        } catch (ex: XmlPullParserException) {
            ex.printStackTrace()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }

    /*
        В этом методе мы подготавливаем XmlPullParser. Для этого вытаскиваем данные из папки
            res/xml. Это аналогично вытаскиванию строк или картинок – сначала получаем доступ к
            ресурсам (getResources), затем вызываем метод, соответствующий ресурсу. В нашем случае
            это - метод getXml. Но возвращает он не xml-строку, а готовый XmlPullParser.
     */
    private fun getParser(): XmlResourceParser {
        return resources.getXml(R.xml.data)
    }

    companion object Constants {
        const val LOG_TAG = "myLogs"
        const val MESSAGE_START_DOCUMENT = "START_DOCUMENT"
        const val MESSAGE_END_DOCUMENT = "END_DOCUMENT"
    }
}