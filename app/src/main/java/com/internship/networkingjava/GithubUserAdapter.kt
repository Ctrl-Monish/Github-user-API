package com.internship.networkingjava

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

class GithubUserAdapter(val githubUsers : ArrayList<GithubUser>) : RecyclerView.Adapter<GithubUserAdapter.GithubViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GithubViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_row, parent, false))

    override fun onBindViewHolder(holder: GithubViewHolder, position: Int) {
        holder?.bind(githubUsers.get(position))
    }

    override fun getItemCount() = githubUsers.size

    class GithubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(githubUser: GithubUser){
            itemView?.tvLogin?.text = githubUser.login
            itemView?.tvhtmlurl?.text = githubUser.html_url
        }

    }
}