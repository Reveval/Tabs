package ru.startandroid.develop.p1012contprovclient.other

import android.net.Uri

const val LOG_TAG = "myLogs"

    /* Constants for DataBase */

//DataBase
const val DB_NAME = "mydb"
const val DB_VERSION = 1
//Table
const val CONTACT_TABLE = "contacts"
//Fields
const val CONTACT_ID = "_id"
const val CONTACT_NAME = "name"
const val CONTACT_EMAIL = "email"

//Script for creation of table
const val DB_CREATE = "create table $CONTACT_TABLE ($CONTACT_ID integer primary key " +
        "autoincrement, $CONTACT_NAME text, $CONTACT_EMAIL text);"

    /* Uri */
/*
    AUTHORITY и CONTACT_PATH – это составные части Uri. Из этих двух констант
        и префикса content:// мы формируем общий Uri - CONTACT_CONTENT_URI. Т.к. здесь не указан
        никакой ID, этот Uri дает доступ ко всем контактам.
 */
//authority
const val AUTHORITY = "unic.ru.startandroid.providers.AdressBook"
//path
const val CONTACT_PATH = "contacts"
//gen uri
val CONTACT_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$CONTACT_PATH")

    /* Data types */
/*
    MIME-типы данных, предоставляемых провайдером. Один для набора данных, другой для конкретной
        записи. Мы будем возвращать их в методе getType нашего провайдера.
 */
//набор строк
const val CONTACT_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$AUTHORITY.$CONTACT_PATH"
//одна строка
const val CONTACT_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$AUTHORITY.$CONTACT_PATH"

    /* UriMatcher */

//общий Uri
const val URI_CONTACTS = 1
//uri с указанным ID
const val URI_CONTACTS_ID = 2