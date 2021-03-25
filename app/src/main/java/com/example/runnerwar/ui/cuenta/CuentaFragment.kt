package com.example.runnerwar.ui.cuenta

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.runnerwar.R
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

        var username : String? = activity?.intent?.extras?.getString("username")

        reg_userName.setText(username.toString())

        boton_edit.setOnClickListener {
            if (reg_userName.isEnabled) {
                reg_userName.setEnabled(false)
                disk_save.setVisibility(View.INVISIBLE)
            }
            else if(!reg_userName.isEnabled){
                reg_userName.setEnabled(true)
                disk_save.setVisibility(View.VISIBLE)
            }
        }
            //val intent = Intent (view.context, SeleccionFaccionActivity::class.java)
            //startActivity(intent)
    }
}