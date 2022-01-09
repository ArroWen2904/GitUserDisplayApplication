package arro.ex.gituserdisplayapplication.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import arro.ex.gituserdisplayapplication.R
import arro.ex.gituserdisplayapplication.repositories.GitUser
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_git_user.view.*

/**
 * Created by Arro on 2022/1/8
 */
class GitUserAdapter(gitUserList: List<GitUser> = listOf()): RecyclerView.Adapter<GitUserViewHolder>() {

    var gitUserList: List<GitUser> = gitUserList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitUserViewHolder {
        return GitUserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_git_user, parent, false))
    }

    override fun onBindViewHolder(holder: GitUserViewHolder, position: Int) {

        Glide.with(holder.itemView.iv_avatar)
            .load(gitUserList[position].avatarUrl)
            .circleCrop()
            .override(holder.itemView.iv_avatar.width, holder.itemView.iv_avatar.height)
            .into(holder.itemView.iv_avatar)

        holder.itemView.tv_user_name.text = gitUserList[position]?.login
        holder.itemView.tv_user_id.text = gitUserList[position]?.id.toString()
    }

    override fun getItemCount(): Int {
        return gitUserList.size
    }

    fun updateGitUserList(newGitUserList: List<GitUser>){
        gitUserList = newGitUserList
        notifyDataSetChanged()
    }

}

class GitUserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
