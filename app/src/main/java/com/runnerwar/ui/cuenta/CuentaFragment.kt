package com.runnerwar.ui.cuenta

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.runnerwar.Data.User.UserDataBase
import com.runnerwar.Factories.UserViewModelFactory
import com.runnerwar.Model.DeleteUser
import com.runnerwar.Model.RegisterResponse
import com.runnerwar.Model.UserUpdate
import com.runnerwar.R
import com.runnerwar.Repositories.UserRepository
import com.runnerwar.ui.buscarCuenta.SearchFragment
import com.runnerwar.ui.cambiarFaccion.CambiarFaccionActivity
import com.runnerwar.ui.registro.RegistroActivity
import com.runnerwar.util.Language
import com.runnerwar.util.Session
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.getstream.chat.android.client.ChatClient
import kotlinx.android.synthetic.main.fragment_cuenta.*


class CuentaFragment : Fragment() {

    private lateinit var cuentaViewModel: CuentaViewModel
    private lateinit var loggedUser: String
    private lateinit var auth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        //Get logged User id

        loggedUser = Session.getIdUsuario()
        //Ini Cuenta viewModel
        val userDao = UserDataBase.getDataBase(activity?.application!!).userDao()
        val repository = UserRepository(userDao, loggedUser.toString())
        val viewModelFactory = UserViewModelFactory(repository,2)
        cuentaViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CuentaViewModel::class.java)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(activity!!, gso)
        auth = Firebase.auth


        val root = inflater.inflate(R.layout.fragment_cuenta, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var actualname: String = "null"

        val cambiar_faccion: Button =  getView()?.findViewById(R.id.cambiarFaccion) as Button
        val search_boton: Button =  getView()?.findViewById(R.id.boton_search) as Button
        val titulo_faccion: TextView =  getView()?.findViewById(R.id.editTextTextPersonName4) as TextView
        val titulo_puntos: TextView =  getView()?.findViewById(R.id.editTextTextPersonName5) as TextView


        if(Language.idioma.equals("castellano")){
            cambiar_faccion.setText("Cambiar faccion")
            search_boton.setText("Buscar usuario")
            titulo_faccion.setText("Faccion")
            titulo_puntos.setText("Puntos")
        }

        else if(Language.idioma.equals("ingles")){
            cambiar_faccion.setText("Change faction")
            search_boton.setText("Search user")
            titulo_faccion.setText("Faction")
            titulo_puntos.setText("Points")
        }

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

        cambiarFaccion.setOnClickListener{
            val intent = Intent(activity?.applicationContext, CambiarFaccionActivity::class.java)
            startActivity(intent)
        }

        cuentaViewModel.readAllData.observe(this@CuentaFragment, Observer { user ->
            if (user != null){
                reg_userCoins.setText(user.coins.toString())
                reg_userName.setText(user.accountname)
                actualname = user.accountname
                reg_email.setText(user._id)
                if(Language.idioma.equals("castellano")){
                    if(user.faction.equals("blue")){
                        reg_faction.setText("azul")
                    }
                    else if(user.faction.equals("red")){
                        reg_faction.setText("rojo")
                    }
                    else if(user.faction.equals("yellow")){
                        reg_faction.setText("amarillo")
                    }
                    else if(user.faction.equals("green")){
                        reg_faction.setText("verde")
                    }
                }
                else{
                    reg_faction.setText(user.faction)
                }
                reg_points.setText(user.points.toString())
            }

        })


        cuentaViewModel.responseUpdate.observe(this@CuentaFragment, Observer { response ->

            if (response.isSuccessful){
                val data: RegisterResponse? = response.body()
                if (data != null) {
                    if (data.codi == 200) {
                        if(Language.idioma.equals("castellano")){
                            Toast.makeText(activity?.applicationContext, "Actualizado satisfactoriamente", Toast.LENGTH_SHORT).show()
                        }

                        else if(Language.idioma.equals("ingles")){
                            Toast.makeText(activity?.applicationContext, "Update successfully", Toast.LENGTH_SHORT).show()
                        }
                        actualname = data.accountname.toString()
                    }
                    else {
                        if(Language.idioma.equals("castellano")){
                            Toast.makeText(activity?.applicationContext, "Nombre de usuario ya en uso", Toast.LENGTH_SHORT).show()
                        }

                        else if(Language.idioma.equals("ingles")){
                            Toast.makeText(activity?.applicationContext, "Accountname already used", Toast.LENGTH_SHORT).show()
                        }
                        reg_userName.setText(actualname)
                    }
                }
            }
        })

        cuentaViewModel.responseDelete.observe(this@CuentaFragment, Observer { response ->

            if (response.isSuccessful){
                val intent = Intent(activity?.applicationContext, RegistroActivity::class.java)
                startActivity(intent)
            }
        })


       disk_save.setOnClickListener {
            reg_userName.setEnabled(false)
            disk_save.setVisibility(View.INVISIBLE)
            val userUp = UserUpdate(reg_userName.text.toString(), reg_email.text.toString())
            if(userUp != null) cuentaViewModel.updateUser(userUp)
        }

        boton_eliminar.setOnClickListener{
            val user = DeleteUser(loggedUser.toString())
            if(user != null) cuentaViewModel.deleteUser(user)
        }

        boton_logout.setOnClickListener {
            val client = ChatClient.instance()
            client.disconnect()
            mGoogleSignInClient.signOut()

            val intent = Intent(activity?.applicationContext, RegistroActivity::class.java)
            startActivity(intent)
        }

        boton_search.setOnClickListener{
            //androidx.appcompat.widget.AppCompatImageButton cannot be cast to android.view.ViewGroup
            val transaction: FragmentTransaction = fragmentManager?.beginTransaction()!!
            transaction.replace(R.id.nav_host_fragment, SearchFragment()).commit()
        }
    }
}