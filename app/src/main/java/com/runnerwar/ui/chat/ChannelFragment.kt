package com.runnerwar.ui.chat

import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.runnerwar.R
import io.getstream.chat.android.client.ChatClient
import com.runnerwar.util.Session
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.client.models.name
import io.getstream.chat.android.livedata.ChatDomain
import io.getstream.chat.android.ui.channel.list.ChannelListView
import io.getstream.chat.android.ui.channel.list.header.ChannelListHeaderView
import io.getstream.chat.android.ui.channel.list.header.viewmodel.ChannelListHeaderViewModel
import io.getstream.chat.android.ui.channel.list.header.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModel
import io.getstream.chat.android.ui.channel.list.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.factory.ChannelListViewModelFactory
import io.getstream.chat.android.ui.databinding.StreamUiFragmentChannelActionsBinding

class ChannelFragment : Fragment() {

    private lateinit var chatViewModel: ChannelViewModel
    private val client = ChatClient.instance()
    private lateinit var userChat: User
    private lateinit var channelView : ChannelListView
    private lateinit var channelHeaderView : ChannelListHeaderView



    private var _binding : StreamUiFragmentChannelActionsBinding? =null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        chatViewModel =
            ViewModelProviders.of(this).get(ChannelViewModel::class.java)
        _binding = StreamUiFragmentChannelActionsBinding.inflate(inflater, container, false)
        val root : View = inflater.inflate(R.layout.fragment_channel, container, false)
        //val textView: TextView = root.findViewById(R.id.text_notifications)
        channelView = root.findViewById(R.id.channelsView)
        channelHeaderView = root.findViewById(R.id.channelListHeaderView)
        val userFragButton= root.findViewById<Button>(R.id.userFragmentButton)

        /*chatViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/

        setUpUser()

        userFragButton.setOnClickListener {
            Log.d("ChatFragment", "Success Connecting the User")
            findNavController().navigate(R.id.action_navigation_chat_to_users)
        }

        channelView.setMoreOptionsClickListener {
            Log.d("ChatFragment", "Algo")
        }

        channelView.setChannelItemClickListener { channel ->

            val action = ChannelFragmentDirections.actionChannelFragmentToChatFragment(channel.cid)
            findNavController().navigate(action)
        }

        channelView.setChannelDeleteClickListener{ channel ->
            deleteChannel(channel)
        }

        return root
    }


    private fun setUpUser() {
        if (client.getCurrentUser() == null){
            var name = Session.getUsername()
            name = name.replace("\\s".toRegex(), "")
            userChat = User(id= name,
                extraData = mutableMapOf(
                    "name" to name,
                    "image" to "https://bit.ly/2TIt8NR"
                ))
            val token = client.devToken(name)
            client.connectUser(userChat, token).enqueue(){result ->
                if( result.isSuccess){
                    Log.d("ChatFragment", "Success Connecting the User")
                    setUpChannnels()
                }else {
                    Log.e("ChatFragment", result.error().message.toString())
                }
            }
        }
        else {
            setUpChannnels()
        }
    }

    private fun setUpChannnels() {
        val filters = Filters.and(
            Filters.eq("type", "messaging"),
            Filters.`in`("members", listOf(client.getCurrentUser()!!.id))
        )

        val viewModelFactory = ChannelListViewModelFactory(filters, ChannelListViewModel.DEFAULT_SORT)
        val listViewModel: ChannelListViewModel by viewModels { viewModelFactory }
        val listHeaderViewModel: ChannelListHeaderViewModel by viewModels()

        listViewModel.bindView(channelView, viewLifecycleOwner)
        listHeaderViewModel.bindView(channelHeaderView, viewLifecycleOwner)
    }

    private fun deleteChannel(channel: Channel){
        ChatDomain.instance().useCases.deleteChannel(channel.cid).enqueue() { result ->
            if (result.isSuccess){
                Log.d("ChatFragment", "${channel.name} removed!")
            }
            else {
                Log.e("ChatFragment", result.error().message.toString())
            }
        }
    }

}