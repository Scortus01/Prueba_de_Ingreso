package com.example.mvvm.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm.R
import com.example.mvvm.data.adapter.RecyclerAdapterPosts
import com.example.mvvm.data.model.PostsModel
import com.example.mvvm.ui.viewmodel.PostsViewModel
import kotlinx.android.synthetic.main.activity_post.*

class ActivityPost : AppCompatActivity(), RecyclerAdapterPosts.OnPostClickListener, View.OnClickListener {

    private lateinit var postsRecyclerView: RecyclerView
    private var postList: List<PostsModel> = emptyList()
    private val postsViewModel: PostsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        initVariables()
        fillRecyclerView()
    }

    private fun initVariables(){
        bnt_back.setOnClickListener(this)
        val id = intent.getStringExtra("id")
        tv_user_post.text = intent.getStringExtra("name")
        tv_phone_post.text = intent.getStringExtra("phone")
        tv_mail_post.text = intent.getStringExtra("email")
        postsRecyclerView = rv_posts
        postsRecyclerView.layoutManager = LinearLayoutManager(this)
        postsRecyclerView.setHasFixedSize(true)
        postsViewModel.onCreate(id!!.toInt())
    }

    private fun fillRecyclerView(){
        postsViewModel.postsModel.observe(this, Observer {
            postList = it
            if (!postList.isNullOrEmpty()){
                postsRecyclerView.adapter = RecyclerAdapterPosts(this,postList)
            }
        })
        postsViewModel.isLoading.observe(this, Observer {
            progress_posts.isVisible = it
            //tv_empty_post.isVisible = it
        })
    }

    private fun pressBack(){

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onButtonClick(id: String?, name: String?, email: String?, phone: String?) {
        //
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.bnt_back ->pressBack()
            }
        }
    }
}