package com.example.mvvm.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm.R
import com.example.mvvm.data.model.PostsModel
import kotlinx.android.synthetic.main.posts_content.view.*
import java.lang.IllegalArgumentException

class RecyclerAdapterPosts(
    private val context: Context,
    private val listPosts: List<PostsModel>
) : RecyclerView.Adapter<BaseViewHolder<*>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return PostsViewHolder(
            LayoutInflater.from(context).inflate(R.layout.posts_content, parent, false)
        )
    }

    interface OnPostClickListener {
        fun onButtonClick(
            id: String?,
            name: String?,
            email: String?,
            phone: String?
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is PostsViewHolder -> holder.bind(listPosts[position], position)
            else -> throw IllegalArgumentException("The viewHolder is wrong")
        }
    }

    override fun getItemCount(): Int {
        return listPosts.size
    }

    inner class PostsViewHolder(itemView: View) : BaseViewHolder<PostsModel>(itemView){
        override fun bind(item: PostsModel, position: Int) {
            itemView.tv_id_user.text = item.userId
            itemView.tv_id_post.text = item.id
            itemView.tv_title.text = item.title
            itemView.tv_body.text = item.body
        }

    }
}