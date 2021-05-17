package com.example.runnerwar.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.runnerwar.R
import com.getstream.sdk.chat.viewmodel.MessageInputViewModel
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel
import io.getstream.chat.android.ui.message.input.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.header.viewmodel.MessageListHeaderViewModel
import io.getstream.chat.android.ui.message.list.header.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.viewmodel.factory.MessageListViewModelFactory
import kotlinx.android.synthetic.main.fragment_chat.view.*




class ChatFragment : Fragment() {

    private val args: ChatFragmentArgs by navArgs()

    private lateinit var root : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_chat, container, false)

        root.messagesHeaderView.setBackButtonClickListener{
            requireActivity().onBackPressed()
        }

        setupMessage()


        return root
    }

    private fun setupMessage() {
        val algo = args.channelId
        val factory = MessageListViewModelFactory(cid = args.channelId)

        val messageListHeaderViewModel: MessageListHeaderViewModel by viewModels { factory }
        val messageListViewModel: MessageListViewModel by viewModels { factory }
        val messageInputViewModel: MessageInputViewModel by viewModels { factory }

        messageListHeaderViewModel.bindView(root.messagesHeaderView, viewLifecycleOwner)
        messageListViewModel.bindView(root.messageList, viewLifecycleOwner)
        messageInputViewModel.bindView(root.messageInputView, viewLifecycleOwner)

    }
}