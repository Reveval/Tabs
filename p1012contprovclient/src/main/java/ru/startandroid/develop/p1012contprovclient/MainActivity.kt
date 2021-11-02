package ru.startandroid.develop.p1012contprovclient

import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private val CONTACT_URI: Uri = Uri.parse(URI)
    /*
        В onCreate мы используем метод getContentResolver, чтобы получить ContentResolver. Этот
            объект – посредник между нами и провайдером. Мы вызываем его метод query и передаем
            туда Uri. Остальные параметры оставляем пустыми – т.е. нам вернутся все записи, все
            поля и сортировку мы не задаем. Полученный курсор мы передаем в Activity на
            управление – метод startManagingCursor. Далее создаем адаптер и присваиваем его списку.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cursor = contentResolver.query(CONTACT_URI, null, null,
            null, null)
        startManagingCursor(cursor)

        val from = arrayOf("name", "email")
        val to = arrayOf(android.R.id.text1, android.R.id.text2).toIntArray()
        val adapter = SimpleCursorAdapter(this, android.R.layout.simple_list_item_2,
            cursor, from, to)
        val listViewContact = findViewById<ListView>(R.id.lvContact)
        listViewContact.adapter = adapter
    }

    /*
        В onClickInsert мы используем метод insert для добавления записей в провайдер.
            Этот метод возвращает нам Uri, соответствующий новой записи.
     */
    fun onClickInsert(view: View) {
        val contentValues = ContentValues().apply {
            put(CONTACT_NAME, "name 4")
            put(CONTACT_EMAIL, "email 4")
        }

        val newUri = contentResolver.insert(CONTACT_URI, contentValues)
        Log.d(LOG_TAG, "insert, result Uri: $newUri")
    }

    /*
        В onClickUpdate мы создаем Uri, соответствующий записи с ID = 2, и апдейтим эту
            запись в провайдере.
     */
    fun onClickUpdate(view: View) {
        val contentValues = ContentValues().apply {
            put(CONTACT_NAME, "name 5")
            put(CONTACT_EMAIL, "email 5")
        }
        val uri = ContentUris.withAppendedId(CONTACT_URI, 2)
        val count = contentResolver.update(uri, contentValues, null, null)
        Log.d(LOG_TAG, "update, count = $count")
    }

    /*
        В onClickDelete мы создаем Uri, соответствующий записи с ID = 3, и удаляем эту
            запись в провайдере.
     */
    fun onClickDelete(view: View) {
        val uri = ContentUris.withAppendedId(CONTACT_URI, 3)
        val count = contentResolver.delete(uri, null, null)
        Log.d(LOG_TAG, "delete, count = $count")
    }

    /*
        В onClickError мы пытаемся получить записи по Uri, который не знает провайдер.
            В его uriMatcher не добавляли информации об этом Uri. В этом случае мы генерировали
            в провайдере ошибку. Здесь попробуем поймать ее.
     */
    fun onClickError(view: View) {
        val uri = Uri.parse(WRONG_URI)
        try {
            val cursor = contentResolver.query(uri, null, null,
                null, null)
        } catch (ex: Exception) {
            Log.d(LOG_TAG, "Error, ${ex.javaClass}, ${ex.message}")
        }
    }

    companion object {
        const val LOG_TAG = "myLogs"
        const val URI = "content://unic.ru.startandroid.providers.AdressBook/contacts"
        const val WRONG_URI = "content://unic.ru.startandroid.providers.AdressBook/phones"
        const val CONTACT_NAME = "name"
        const val CONTACT_EMAIL = "email"
    }
}