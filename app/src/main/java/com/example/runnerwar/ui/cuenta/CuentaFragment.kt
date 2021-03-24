package com.example.runnerwar.ui.cuenta

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.runnerwar.R
import com.example.runnerwar.ui.seleccionFaccion.SeleccionFaccionActivity
import kotlinx.android.synthetic.main.fragment_cuenta.*

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        boton_edit.setOnClickListener {
            if (reg_userName.isEnabled) {
                reg_userName.setEnabled(false)
            }
            else if(!reg_userName.isEnabled){
                reg_userName.setEnabled(true)
            }
        }
            //val intent = Intent (view.context, SeleccionFaccionActivity::class.java)
            //startActivity(intent)
    }
}