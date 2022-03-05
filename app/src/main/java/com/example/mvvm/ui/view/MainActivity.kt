package com.example.mvvm.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
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

/**
 * Clase principal donde se muestra la interfaz grafica de las publicaciones
 * genera la lógica para el funcionamiento de los datos de las publicaciones
 */
class MainActivity : AppCompatActivity(), RecyclerAdapter.OnUserClickListener,
    androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var usersSearchText: androidx.appcompat.widget.SearchView
    private var usersList: List<UsersModel> = emptyList()
    private val userViewModel: UsersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        setContentView(binding.root)

        initVariables()
        checkLocalData()
    }

    private fun checkLocalData() {
        usersList = getAllUsersSQLite()
        if (!usersList.isNullOrEmpty()) {
            fillRecyclerViewLocalData(usersList)
        } else {
            fillRecyclerViewAPIRest()
        }
    }

    private fun initVariables() {
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

    private fun fillRecyclerViewAPIRest() {
        userViewModel.usersModel.observe(this, {
            usersList = it
            if (!usersList.isNullOrEmpty()) {
                setQuery(usersList)
                usersRecyclerView.adapter = RecyclerAdapter(this, this, usersList)
            }
        })
        userViewModel.isLoading.observe(this, {
            progress.isVisible = it
        })
    }

    private fun filterByName(query: String): List<UsersModel> {

        return usersList.filter {
            it.name.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
        }
    }

    private fun setQuery(usersList: List<UsersModel>) {
        val database = DBHelper(this, null)
        for (data in usersList) {
            database.addUser(data)
        }
    }

    private fun getAllUsersSQLite(): List<UsersModel> {
        val user: MutableList<UsersModel> = arrayListOf()
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
        if (!newText.isNullOrEmpty() && !usersList.isNullOrEmpty()) {
            usersRecyclerView.adapter = RecyclerAdapter(this, this, filterByName(newText))
        } else {
            usersRecyclerView.adapter = RecyclerAdapter(this, this, usersList)
        }
        return false
    }
}