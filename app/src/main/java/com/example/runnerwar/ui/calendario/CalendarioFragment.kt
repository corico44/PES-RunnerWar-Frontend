package com.example.runnerwar.ui.calendario

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.runnerwar.R

class CalendarioFragment : Fragment() {

    private lateinit var calendarioViewModel: CalendarioViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calendarioViewModel =
            ViewModelProviders.of(this).get(CalendarioViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_channel, container, false)
        //val textView: TextView = root.findViewById(R.id.text_notifications)
        /*calendarioViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        return root
    }
}