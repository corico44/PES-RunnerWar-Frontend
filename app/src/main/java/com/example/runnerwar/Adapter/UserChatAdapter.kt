package com.example.runnerwar.Adapter

import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.runnerwar.R

import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.ui.avatar.AvatarView
import kotlinx.android.synthetic.main.user_row_layout.view.*

import androidx.appcompat.app.AppCompatActivity
import com.example.runnerwar.ui.chat.ChatFragment
import com.example.runnerwar.ui.chat.UsersFragment
import com.example.runnerwar.ui.chat.UsersFragmentDirections


class UserChatAdapter  : RecyclerView.Adapter<UserChatAdapter.ViewHolder>(){

    private val client = ChatClient.instance()
    private var userList = emptyList<User>()

    class ViewHolder( val itemView: View  ) : RecyclerView.ViewHolder(itemView) {
        val itemUsername: TextView = itemView.findViewById(R.id.username_textView)
        val itemLastActive: TextView = itemView.findViewById(R.id.lastActive_textView)
        val itemPicture : AvatarView = itemView.findViewById(R.id.avatar_imageView)
        val createButton : ConstraintLayout  = itemView.findViewById(R.id.rootLayout)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v : View = LayoutInflater.from(parent.context).inflate(R.layout.user_row_layout, parent, false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentUser = userList[position]

        holder.itemPicture .setUserData(currentUser)
        holder.itemUsername.text = currentUser.id
        holder.itemLastActive.text = convertDate(currentUser.lastActive!!.time)

        holder.createButton.setOnClickListener {
            createNewChannel(currentUser.id, it)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(newList: List<User>) {
        userList = newList
        notifyDataSetChanged()
    }

    private fun convertDate(milliseconds: Long): String {
        return DateFormat.format("dd/MM/yyyy hh:mm a", milliseconds).toString()
    }

    private fun createNewChannel(selectedUser: String, holder: View) {
        client.createChannel(
            channelType = "messaging",
            members = listOf(client.getCurrentUser()!!.id, selectedUser)
        ).enqueue { result ->
            if (result.isSuccess) {
                navigateToChatFragment(holder, result.data().cid)
            } else {
                Log.e("UsersAdapter", result.error().message.toString())
            }
        }
    }

   private fun navigateToChatFragment(holder: View, cid: String) {
       val action = UsersFragmentDirections.actionUsersFragmentToChatFragment(cid)
       holder.findNavController().navigate(action)
   }


}