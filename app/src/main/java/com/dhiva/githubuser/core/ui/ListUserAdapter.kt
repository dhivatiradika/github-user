package com.dhiva.githubuser.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dhiva.githubuser.R
import com.dhiva.githubuser.databinding.ItemUserBinding
import com.dhiva.githubuser.core.utils.loadImage

class ListUserAdapter(private val listUsers: List<com.dhiva.githubuser.core.data.source.remote.response.User>) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ListViewHolder(view)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUsers[position]

        holder.binding.tvUsername.text = user.login
        holder.binding.ivProfile.loadImage(user.avatarUrl)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(user) }

    }

    override fun getItemCount(): Int = listUsers.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = ItemUserBinding.bind(itemView)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: com.dhiva.githubuser.core.data.source.remote.response.User)
    }
}