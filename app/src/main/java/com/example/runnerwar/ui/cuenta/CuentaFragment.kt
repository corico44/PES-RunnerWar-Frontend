package com.example.runnerwar.ui.cuenta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.runnerwar.R

class CuentaFragment : Fragment() {

    private lateinit var cuentaViewModel: CuentaViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cuentaViewModel =
            ViewModelProviders.of(this).get(CuentaViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_cuenta, container, false)
        return root
    }
}