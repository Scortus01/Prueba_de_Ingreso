package com.example.mvvm.core

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mvvm.data.model.PostsModel
import com.example.mvvm.data.model.UsersModel

/**
 * Clase encargada de Gestionar la base de datos SQLite
 */
class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {

        val CREATE_TABLE_USER = ("CREATE TABLE " + USER_TABLE_NAME + " ("
                + USER_ID_COL + " TEXT PRIMARY KEY, " +
                USER_NAME_COL + " TEXT," +
                USER_PHONE_COL + " TEXT, " +
                USER_EMAIL_COL + " TEXT" + ")")

        val CREATE_TABLE_POST = ("CREATE TABLE " + POST_TABLE_NAME + " ("
                + POST_ID_COL + " TEXT PRIMARY KEY, " +
                POST_TITLE_COL + " TEXT," +
                POST_BODY_COL + " TEXT, " +
                POST_USER_ID_COL + " TEXT REFERENCES User (id)" + ")")

        db.execSQL(CREATE_TABLE_USER)
        db.execSQL(CREATE_TABLE_POST)

    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {

        db.execSQL("DROP TABLE IF EXISTS $USER_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $POST_TABLE_NAME")
        onCreate(db)
    }

    fun addUser(user: UsersModel) {
        val values = ContentValues()
        values.put(USER_ID_COL, user.id)
        values.put(USER_NAME_COL, user.name)
        values.put(USER_PHONE_COL, user.phone)
        values.put(USER_EMAIL_COL, user.email)

        val db = this.writableDatabase

        db.insert(USER_TABLE_NAME, null, values)

        db.close()
    }

    fun addPost(post: PostsModel) {
        val values = ContentValues()
        values.put(POST_USER_ID_COL, post.userId)
        values.put(POST_ID_COL, post.id)
        values.put(POST_TITLE_COL, post.title)
        values.put(POST_BODY_COL, post.body)

        val db = this.writableDatabase

        db.insert(POST_TABLE_NAME, null, values)

        db.close()
    }

    fun getAllUsers(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM " + USER_TABLE_NAME, null)
    }

    fun getPostsById(postId: String): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM " + POST_TABLE_NAME + " WHERE userId=" + postId, null)
    }

    companion object {

        private val DATABASE_NAME = "DATABASE_PRUEBA_CEIBA"

        private val DATABASE_VERSION = 1

        val USER_TABLE_NAME = "User"
        val POST_TABLE_NAME = "Post"

        //Propiedades del ususario
        val USER_ID_COL = "id"
        val USER_NAME_COL = "name"
        val USER_PHONE_COL = "phone"
        val USER_EMAIL_COL = "email"

        // propiedades de la publicación
        val POST_ID_COL = "id"
        val POST_TITLE_COL = "title"
        val POST_BODY_COL = "body"
        val POST_USER_ID_COL = "userId"

    }
}