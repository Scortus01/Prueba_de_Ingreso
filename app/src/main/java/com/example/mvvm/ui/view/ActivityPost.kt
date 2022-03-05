package com.example.mvvm.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm.R
import com.example.mvvm.core.DBHelper
import com.example.mvvm.data.adapter.RecyclerAdapterPosts
import com.example.mvvm.data.model.PostsModel
import com.example.mvvm.ui.viewmodel.PostsViewModel
import kotlinx.android.synthetic.main.activity_post.*

/**
 * Clase donde se muestra la interfaz grafica de las publicaciones
 * genera la lógica para el funcionamiento de los datos de las publicaciones
 */
class ActivityPost : AppCompatActivity(), RecyclerAdapterPosts.OnPostClickListener,
    View.OnClickListener {

    private lateinit var postsRecyclerView: RecyclerView
    private var postList: List<PostsModel> = emptyList()
    private val postsViewModel: PostsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        initVariables()
    }

    private fun initVariables() {
        bnt_back.setOnClickListener(this)
        val id = intent.getStringExtra("id")
        tv_user_post.text = intent.getStringExtra("name")
        tv_phone_post.text = intent.getStringExtra("phone")
        tv_mail_post.text = intent.getStringExtra("email")
        postsRecyclerView = rv_posts
        postsRecyclerView.layoutManager = LinearLayoutManager(this)
        postsRecyclerView.setHasFixedSize(true)
        postsViewModel.onCreate(id!!.toInt())
        checkLocalData(id)
    }

    private fun setQuery(posts: List<PostsModel>) {
        val database = DBHelper(this, null)
        for (data in posts) {
            database.addPost(data)
        }
    }

    private fun fillRecyclerView() {
        postsViewModel.postsModel.observe(this, {
            postList = it
            if (!postList.isNullOrEmpty()) {
                setQuery(postList)
                postsRecyclerView.adapter = RecyclerAdapterPosts(this, postList)
            }
        })
        postsViewModel.isLoading.observe(this, {
            progress_posts.isVisible = it
        })
    }

    private fun fillRecyclerViewLocalData() {
        postsRecyclerView.adapter = RecyclerAdapterPosts(this, postList)
    }

    private fun checkLocalData(id: String){
        postList = getAllPostsUserID(id)
        if (!postList.isNullOrEmpty()) fillRecyclerViewLocalData()
        else fillRecyclerView()
    }

    private fun getAllPostsUserID(id: String): List<PostsModel> {
        val post: MutableList<PostsModel> = arrayListOf()
        val database = DBHelper(this, null)
        val data = database.getPostsById(id)
        if (data!!.moveToFirst()){
            while (!data.isAfterLast){
                val userId = data.getString(data.getColumnIndexOrThrow("userId"))
                val id = data.getString(data.getColumnIndexOrThrow("id"))
                val title = data.getString(data.getColumnIndexOrThrow("title"))
                val body = data.getString(data.getColumnIndexOrThrow("body"))
                post.add(PostsModel(userId, id, title, body))
                data.moveToNext()
            }
        }
        data.close()
        return post
    }

    private fun pressBack() {

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onButtonClick(id: String?, name: String?, email: String?, phone: String?) {
        //
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.bnt_back -> pressBack()
            }
        }
    }
}