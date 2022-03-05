package com.example.mvvm.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm.R
import com.example.mvvm.data.model.UsersModel
import java.lang.IllegalArgumentException
import kotlinx.android.synthetic.main.users_content.view.*

/**
 * Clase encargada de adaptar la información obtenida de los usuarios en la interfaz gráfica
 */
class RecyclerAdapter(
    private val context: Context,
    private val itemClickListener: OnUserClickListener,
    private val listUsers: List<UsersModel>
) : RecyclerView.Adapter<BaseViewHolder<*>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return UsersViewHolder(
            LayoutInflater.from(context).inflate(R.layout.users_content, parent, false)
        )
    }

    interface OnUserClickListener {
        fun onButtonClick(
            id: String?,
            name: String?,
            email: String?,
            phone: String?
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is UsersViewHolder -> holder.bind(listUsers[position], position)
            else -> throw IllegalArgumentException("The viewHolder is wrong")
        }
    }

    override fun getItemCount(): Int {
        return listUsers.size
    }

    inner class UsersViewHolder(itemView: View) : BaseViewHolder<UsersModel>(itemView) {
        override fun bind(item: UsersModel, position: Int) {
            itemView.tv_post.setOnClickListener {
                itemClickListener.onButtonClick(
                    item.id,
                    item.name,
                    item.email,
                    item.phone
                )
            }
            itemView.tv_user.text = item.name
            itemView.tv_mail.text = item.email
            itemView.tv_phone.text = item.phone
        }
    }
}