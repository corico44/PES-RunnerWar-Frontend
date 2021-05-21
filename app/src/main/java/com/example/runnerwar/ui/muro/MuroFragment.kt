package com.example.runnerwar.ui.muro

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.runnerwar.R
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.runnerwar.Data.User.UserDataBase
import com.example.runnerwar.Factories.UserViewModelFactory
import com.example.runnerwar.Model.Codi
import com.example.runnerwar.Model.MailForm
import com.example.runnerwar.Repositories.UserRepository
import com.example.runnerwar.util.Session
import kotlinx.android.synthetic.main.fragment_muro.*


class MuroFragment : Fragment() {

    private lateinit var muroViewModel: MuroViewModel
    private val itemsList: List<Items> = ArrayList()
    private var listView: ListView? = null
    private var adapter: CustomListAdapter? = null

    override fun onStart() {

        super.onStart()
            //openPopUpDailyLogin()
            println("EL CORREO ES: " + Session.getIdUsuario().toString())
            val mail = MailForm(Session.getIdUsuario().toString())
            muroViewModel.dailyLogin(mail)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var loggedUser : String? = activity?.intent?.extras?.getString("email")

        //Ini Cuenta viewModel
        val userDao = UserDataBase.getDataBase(activity?.application!!).userDao()
        val repository = UserRepository(userDao, loggedUser.toString())
        val viewModelFactory = UserViewModelFactory(repository,5)
        muroViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(MuroViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_muro, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)

        muroViewModel.responseDaily.observe(viewLifecycleOwner, Observer {
            var successCode = Codi(200)
            if(it == successCode){
                openPopUpDailyLogin()
            }
        })


        listView = list_score
        adapter = activity?.applicationContext?.let { CustomListAdapter(it, itemsList) }
        listView?.setAdapter(adapter)


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        muroViewModel.responseDaily.observe(this@MuroFragment, Observer { response ->
        })
    }

    fun openPopUpDailyLogin(){
        val dialogBuilder = AlertDialog.Builder(activity)

        // set message of alert dialog
        dialogBuilder.setMessage("This is your daily login award!")
            // if the dialog is cancelable
            .setCancelable(false)
            .setMessage("Congratulations you earned:")
            .setMessage("+20 points")
            // negative button text and action
            .setNegativeButton("Okey", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("DAILY LOGIN AWARD!")
        // show alert dialog
        alert.show()
    }
}