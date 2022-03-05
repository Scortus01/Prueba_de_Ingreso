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
import com.example.mvvm.core.DBHelper
import com.example.mvvm.data.adapter.RecyclerAdapter
import com.example.mvvm.data.model.UsersModel
import com.example.mvvm.databinding.ActivityMainBinding
import com.example.mvvm.ui.viewmodel.UsersViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), RecyclerAdapter.OnUserClickListener,
    androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var usersSearchText: androidx.appcompat.widget.SearchView
    private var usersList: List<UsersModel> = emptyList()
    private var usersListLocal: List<UsersModel> = emptyList()
    private val userViewModel: UsersViewModel by viewModels()
    private var isLocal = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        setContentView(binding.root)

        initVariables()
        checkLocalData()
    }

    private fun checkLocalData(){
        usersListLocal = getAllUsersSQLite()
        isLocal = if(usersListLocal.isNullOrEmpty()) {
            fillRecyclerViewAPIRest()
            true
        } else{
            fillRecyclerViewLocalData(usersListLocal)
            false
        }
    }

    private fun initVariables(){
        usersRecyclerView = rv_users
        usersSearchText = sv_users
        usersSearchText.setOnQueryTextListener(this)
        usersRecyclerView.layoutManager = LinearLayoutManager(this)
        usersRecyclerView.setHasFixedSize(true)
        userViewModel.onCreate()
    }

    private fun fillRecyclerViewLocalData(localData: List<UsersModel>) {
        usersRecyclerView.adapter = RecyclerAdapter(this, this, localData)
    }

    private fun fillRecyclerViewAPIRest(){
        userViewModel.usersModel.observe(this, Observer {
            usersList = it
            if (!usersList.isNullOrEmpty()){
                setQuery(usersList)
                usersRecyclerView.adapter = RecyclerAdapter(this, this, usersList)
            }
        })
        userViewModel.isLoading.observe(this, Observer {
            progress.isVisible = it
        })
    }

    private fun filterByName(query: String): List<UsersModel>{

        return if (isLocal){
            filterByDataType(usersListLocal, query)
        }else{
            filterByDataType(usersList, query)
        }

    }

    private fun filterByDataType(data: List<UsersModel>, query: String): List<UsersModel>{

        val listFilter = data.filter {
            it.name.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
        }
        if (listFilter.isNullOrEmpty()) tv_empty_.visibility = View.VISIBLE
        else tv_empty_.visibility = View.INVISIBLE
        return listFilter
    }

    private fun setQuery(usersList: List<UsersModel>) {
        val database = DBHelper(this, null)
        for (data in usersList){
            database.addUser(data)
        }
    }

    private fun getAllUsersSQLite(): List<UsersModel>{
        var user : MutableList<UsersModel> = arrayListOf()
        val database = DBHelper(this, null)
        val data = database.getAllUsers()
        if (data!!.moveToFirst()) {
            while (!data.isAfterLast) {
                val id: String = data.getString(data.getColumnIndexOrThrow("id"))
                val name: String = data.getString(data.getColumnIndexOrThrow("name"))
                val email: String = data.getString(data.getColumnIndexOrThrow("email"))
                val phone: String = data.getString(data.getColumnIndexOrThrow("phone"))
                user.add(UsersModel(id, name, email, phone))
                data.moveToNext()
            }
        }
        data.close()
        return user
    }

    override fun onButtonClick(
        id: String?,
        name: String?,
        email: String?,
        phone: String?
    ) {
        val intent = Intent(this, ActivityPost::class.java)
        intent.putExtra("id", id)
        intent.putExtra("name", name)
        intent.putExtra("phone", phone)
        intent.putExtra("email", email)
        startActivity(intent)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (!newText.isNullOrEmpty() && !usersList.isNullOrEmpty()){
            usersRecyclerView.adapter = RecyclerAdapter(this, this, filterByName(newText))
        }else{
            usersRecyclerView.adapter = RecyclerAdapter(this, this, usersListLocal)
        }
        return false
    }
}