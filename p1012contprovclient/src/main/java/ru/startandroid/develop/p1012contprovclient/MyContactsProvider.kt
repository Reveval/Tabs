package ru.startandroid.develop.p1012contprovclient

import android.content.*
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import ru.startandroid.develop.p1012contprovclient.other.*

class MyContactsProvider : ContentProvider() {
    /*
        Далее создаем и описываем UriMatcher и константы для него. UriMatcher – это что-то типа
            парсера. В методе addURI мы даем ему комбинацию: authority, path и константа. Причем,
            мы можем использовать спецсимволы: * - строка любых символов любой длины, # - строка
            цифр любой длины. На вход провайдеру будут поступать Uri, и мы будем сдавать их в
            UriMatcher на проверку. Если Uri будет подходить под комбинацию authority и path, ранее
            добавленных в addURI, то UriMatcher вернет константу из того же набора: authority,
            path, константа.
     */
    companion object  {
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, CONTACT_PATH, URI_CONTACTS)
            addURI(AUTHORITY, "$CONTACT_PATH/#", URI_CONTACTS_ID)
        }
    }

    lateinit var dbHelper: DBHelper
    lateinit var db: SQLiteDatabase
    var customSelect : String? = null

    //В OnCreate создаем DBHelper – уже знакомый нам помощник для работы с БД
    override fun onCreate(): Boolean {
        Log.d(LOG_TAG, "onCreate")
        dbHelper = DBHelper(context)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        var mySortOrder = sortOrder
        customSelect = selection

        Log.d(LOG_TAG, "query $uri")
        //проверяем Uri
        when(uriMatcher.match(uri)) {
            //общий uri
            URI_CONTACTS -> {
                Log.d(LOG_TAG, "URI_CONTACTS")
                //если сортрировка не указана, ставим свою - по имени
                if (TextUtils.isEmpty(mySortOrder)) mySortOrder = "$CONTACT_NAME ASC"
            }

            //Uri с ID
            URI_CONTACTS_ID -> {
                val id = uri.lastPathSegment
                Log.d(LOG_TAG, "URI_CONTACTS_ID, $id")
                //добаляем id к условию выборки
                if (TextUtils.isEmpty(selection)) {
                    customSelect = "$CONTACT_ID = $id"
                } else {
                    customSelect += " AND $CONTACT_ID = $id"
                }
            }

            else -> throw IllegalArgumentException("Wrong Uri: $uri")
        }
        db = dbHelper.writableDatabase
        val cursor = db.query(
            CONTACT_TABLE, projection, customSelect, selectionArgs,
            null, null, mySortOrder)
        return cursor.also {
            it.setNotificationUri(context?.contentResolver, CONTACT_CONTENT_URI)
        }
    }

    //В методе getType возвращаем типы соответственно типу Uri – общий или с ID.
    override fun getType(uri: Uri): String? {
        Log.d(LOG_TAG, "getType, $uri")
        return when(uriMatcher.match(uri)) {
            URI_CONTACTS -> CONTACT_CONTENT_TYPE
            URI_CONTACTS_ID -> CONTACT_CONTENT_ITEM_TYPE
            else -> null
        }
    }

    /*
        В insert мы проверяем, что нам пришел наш общий Uri. Если все ок, то вставляем данные в
            таблицу, получаем ID. Этот ID мы добавляем к общему Uri и получаем Uri с ID. По идее,
            это можно сделать и обычным сложением строк, но рекомендуется использовать метод
            withAppendedId объекта. Далее мы уведомляем систему, что поменяли данные,
            соответствующие resultUri. Система посмотрит, не зарегистрировано ли слушателей на
            этот Uri. Увидит, что мы регистрировали курсор, и даст ему знать, что данные
            обновились. В конце мы возвращаем resultUri, соответствующий новой добавленной записи.
     */
    override fun insert(uri: Uri, values: ContentValues?): Uri {
        Log.d(LOG_TAG, "insert, $uri")
        if (uriMatcher.match(uri) != URI_CONTACTS) {
            throw IllegalArgumentException("Wrong URI: $uri")
        }
        db = dbHelper.writableDatabase
        val rowId = db.insert(CONTACT_TABLE, null, values)
        val resultUri: Uri = ContentUris.withAppendedId(CONTACT_CONTENT_URI, rowId)
        //уведомляем ContentResolver, что данные по адресу resultUri изменились
        context?.contentResolver?.notifyChange(resultUri, null)
        return resultUri
    }

    /*
        В delete мы проверяем, какой Uri нам пришел. Если с ID, то фиксим selection – добавляем
            туда условие по ID. Выполняем удаление в БД, получаем кол-во удаленных записей.
            Уведомляем, что данные изменились. Возвращаем кол-во удаленных записей.
     */
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        customSelect = selection
        Log.d(LOG_TAG, "delete, $uri")
        when(uriMatcher.match(uri)) {
            URI_CONTACTS -> Log.d(LOG_TAG, "URI_CONTACTS")
            URI_CONTACTS_ID -> {
                val id = uri.lastPathSegment
                Log.d(LOG_TAG, "URI_CONTACTS_ID, $id")
                if (TextUtils.isEmpty(customSelect)) {
                    customSelect = "$CONTACT_ID = $id"
                } else {
                    customSelect += " AND $CONTACT_ID = $id"
                }
            }
            else -> throw IllegalArgumentException("Wrong Uri: $uri")
        }
        db = dbHelper.writableDatabase
        val count = db.delete(CONTACT_TABLE, customSelect, selectionArgs)
        context?.contentResolver?.notifyChange(uri, null)
        return count
    }

    /*
        В update мы проверяем, какой Uri нам пришел. Если с ID, то фиксим selection – добавляем
            туда условие по ID. Выполняем обновление в БД, получаем кол-во обновленных записей.
            Уведомляем, что данные изменились. Возвращаем кол-во обновленных записей.
     */
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        customSelect = selection
        Log.d(LOG_TAG, "update, $uri")
        when(uriMatcher.match(uri)) {
            URI_CONTACTS -> Log.d(LOG_TAG, "URI_CONTACTS")
            URI_CONTACTS_ID -> {
                val id = uri.lastPathSegment
                Log.d(LOG_TAG, "URI_CONTACTS_ID, $id")
                if (TextUtils.isEmpty(customSelect)) {
                    customSelect = "$CONTACT_ID = $id"
                } else {
                    customSelect += " AND $CONTACT_ID = $id"
                }
            }
            else -> throw  IllegalArgumentException("Wrong Uri: $uri")
        }
        db = dbHelper.writableDatabase
        val count = db.update(CONTACT_TABLE, values, customSelect, selectionArgs)
        context?.contentResolver?.notifyChange(uri, null)
        return count
    }

    class DBHelper(context: Context?) :
        SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(DB_CREATE)
            val cv = ContentValues()
            for (i in 1..3) {
                cv.put(CONTACT_NAME, "name $i")
                cv.put(CONTACT_EMAIL, "email $i")
                db?.insert(CONTACT_TABLE, null, cv)
            }
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
    }
}