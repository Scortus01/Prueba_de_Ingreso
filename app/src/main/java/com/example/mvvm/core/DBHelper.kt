package com.example.mvvm.core

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mvvm.data.model.PostsModel
import com.example.mvvm.data.model.UsersModel

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    // below is the method for creating a database by a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // below is a sqlite query, where column names
        // along with their data types is given
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

        // we are calling sqlite
        // method for executing our query
        db.execSQL(CREATE_TABLE_USER)
        db.execSQL(CREATE_TABLE_POST)

    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + POST_TABLE_NAME)
        onCreate(db)
    }

    fun addUser(user: UsersModel){
        val values = ContentValues()
        values.put(USER_ID_COL, user.id)
        values.put(USER_NAME_COL, user.name)
        values.put(USER_PHONE_COL, user.phone)
        values.put(USER_EMAIL_COL, user.email)

        val db = this.writableDatabase

        db.insert(USER_TABLE_NAME, null, values)

        db.close()
    }

    fun addPost(post: PostsModel){
        val values = ContentValues()
        values.put(POST_ID_COL, post.id)
        values.put(POST_TITLE_COL, post.title)
        values.put(POST_BODY_COL, post.body)
        values.put(POST_USER_ID_COL, post.userId)

        val db = this.writableDatabase

        db.insert(POST_TABLE_NAME, null, values)

        db.close()
    }

    fun getAllUsers(): Cursor?{
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM " + USER_TABLE_NAME, null)
    }

    fun getPostsById(postId: String): Cursor?{
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM " + POST_TABLE_NAME + " WHERE id=" + postId, null)
    }

    companion object{
        // here we have defined variables for our database

        // below is variable for database name
        private val DATABASE_NAME = "DATABASE_PRUEBA_CEIBA"

        // below is the variable for database version
        private val DATABASE_VERSION = 1

        // below is the variable for table name
        val USER_TABLE_NAME = "User"
        val POST_TABLE_NAME = "Post"

        // propiedades de User
        val USER_ID_COL = "id"
        val USER_NAME_COL = "name"
        val USER_PHONE_COL = "phone"
        val USER_EMAIL_COL = "email"

        // propiedades de Post
        val POST_ID_COL = "id"
        val POST_TITLE_COL = "title"
        val POST_BODY_COL = "body"
        val POST_USER_ID_COL = "userId"

    }
}