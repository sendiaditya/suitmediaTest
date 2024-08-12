package com.magang.suitmediatest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.magang.suitmediatest.api.DataItem

class UserAdapter(private val onUserClick: (DataItem) -> Unit) : ListAdapter<DataItem, UserAdapter.UserViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_row, parent, false)
        return UserViewHolder(view, onUserClick)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class UserViewHolder(itemView: View, private val onUserClick: (DataItem) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val tvFirstname = itemView.findViewById<TextView>(R.id.tvFirstname)
        private val tvLastname = itemView.findViewById<TextView>(R.id.tvLastname)
        private val tvEmail = itemView.findViewById<TextView>(R.id.tvEmail)
        private val ivUserPhoto = itemView.findViewById<ShapeableImageView>(R.id.ivUserPhoto)

        fun bind(user: DataItem) {
            tvFirstname.text = user.firstName
            tvLastname.text = user.lastName
            tvEmail.text = user.email
            Glide.with(itemView.context)
                .load(user.avatar)
                .into(ivUserPhoto)

            itemView.setOnClickListener {
                onUserClick(user)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}