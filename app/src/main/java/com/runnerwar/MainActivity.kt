package com.runnerwar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.runnerwar.R
import com.runnerwar.ui.login.LoginActivity

import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.livedata.ChatDomain

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val client = ChatClient.Builder(getString(R.string.api_key), this).logLevel(ChatLogLevel.ALL).build()
        ChatDomain.Builder(client, this).build()

        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.putExtra("some_error", " ")
        startActivity(intent)


    }
}