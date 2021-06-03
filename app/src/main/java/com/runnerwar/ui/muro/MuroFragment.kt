package com.runnerwar.ui.muro

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.runnerwar.Data.User.UserDataBase
import com.runnerwar.Factories.UserViewModelFactory
import com.runnerwar.Model.Codi
import com.runnerwar.Model.MailForm
import com.runnerwar.R
import com.runnerwar.Repositories.UserRepository
import com.runnerwar.util.Language
import com.runnerwar.util.Session
import com.runnerwar.Model.UserLeaderboards
import kotlinx.android.synthetic.main.fragment_muro.*


class MuroFragment : Fragment() {

    private lateinit var muroViewModel: MuroViewModel
    private var listView: ListView? = null
    private var listUsers : List<UserLeaderboards>? = null

    override fun onStart() {
        super.onStart()
        //openPopUpDailyLogin()
        // use arrayadapter and define an array


        muroViewModel.leaderboardsUsers()
        muroViewModel.leaderboardsFactions()

        //muroViewModel.responseUsers.observe(this@MuroFragment, Observer {
            //listUsers = it
            /*MostraValors()*/
            //AfegirValors()
        //})
        muroViewModel.responseFactions.observe(this@MuroFragment, Observer {
            var blue = it.blue
            var green = it.green
            var red = it.red
            var yellow= it.yellow
            MostraTeams(blue,green,red,yellow)
        })


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

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        muroViewModel.responseDaily.observe(viewLifecycleOwner, Observer { response ->
        })

    }

    fun openPopUpDailyLogin(){
        val dialogBuilder = AlertDialog.Builder(activity)
        var titulo = ""

        // set message of alert dialog
        if(Language.idioma.equals("castellano")){
            dialogBuilder.setMessage("¡Este es su premio de inicio de sesión diario!")
                // if the dialog is cancelable
                .setCancelable(false)
                .setMessage("Felicidades, has ganado:")
                .setMessage("+20 puntos")
                // negative button text and action
                .setNegativeButton("De acuerdo", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })
            titulo = "¡PREMIO DE INICIO DE SESIÓN DIARIO!"
        }
        if(Language.idioma.equals("ingles")){
            dialogBuilder.setMessage("This is your daily login award!")
                // if the dialog is cancelable
                .setCancelable(false)
                .setMessage("Congratulations, you earned:")
                .setMessage("+20 points")
                // negative button text and action
                .setNegativeButton("Okey", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })
            titulo = "DAILY LOGIN AWARD!"
        }


        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle(titulo)
        // show alert dialog
        alert.show()
    }

    /*fun MostraValors(){
        for (elem in listUsers!!){
            Log.d("Username", elem.accountname)
            Log.d("Points", elem.points.toString())
            Log.d("Coins", elem.coins.toString())
        }
    }*/
    fun AfegirValors(){

        val list = SCHEDULE as ListView
        val mylist = ArrayList<HashMap<String, String?>>()
        var map = HashMap<String, String?>()
        var contador = 1

        for (elem in listUsers!!){
            map["numcell"] = contador.toString()
            map["train"] = elem.accountname
            map["from"] = elem.coins.toString()
            mylist.add(map)
            map = HashMap()
            contador++
        }


        var mSchedule = SimpleAdapter(
            activity?.applicationContext,
            mylist,
            R.layout.row,
            arrayOf("numcell","train", "from"),
            intArrayOf(
                R.id.NUM_CELL,
                R.id.TRAIN_CELL,
                R.id.FROM_CELL,
            )
        )
        list.adapter = mSchedule

    }
    private fun MostraTeams(blue:Int, green:Int, red:Int, yellow:Int) {
        val list = SCHEDULE as ListView
        val mylist = ArrayList<HashMap<String, String?>>()
        var map = HashMap<String, String?>()
        var contador = 1

        val map2 = mapOf(Pair("blue", blue), Pair("red", red), Pair("yellow", yellow),Pair("green",green))
        val result = map2.toList().sortedBy{(_,value) -> value}

         map["numcell"] = contador.toString()
         map["train"] = result[3].first
         map["from"] = result[3].second.toString()
         mylist.add(map)
         map = HashMap()
         contador++
        map["numcell"] = contador.toString()
        map["train"] = result[2].first
        map["from"] = result[2].second.toString()
        mylist.add(map)
        map = HashMap()
        contador++
        map["numcell"] = contador.toString()
        map["train"] = result[1].first
        map["from"] = result[1].second.toString()
        mylist.add(map)
        map = HashMap()
        contador++
        map["numcell"] = contador.toString()
        map["train"] = result[0].first
        map["from"] = result[0].second.toString()
        mylist.add(map)
        map = HashMap()
        contador++

     //}*/

        /*map["numcell"] = blue.toString()
        map["train"] = "Blue"
        mylist.add(map)
        map = HashMap()
        map["numcell"] = red.toString()
        map["train"] = "Red"
        mylist.add(map)
        map = HashMap()
        map["numcell"] = green.toString()
        map["train"] = "Green"
        mylist.add(map)
        map = HashMap()
        map["numcell"] = yellow.toString()
        map["train"] = "Yellow"
        map = HashMap()*/


        var mSchedule = SimpleAdapter(
            activity?.applicationContext,
            mylist,
            R.layout.row2,
            arrayOf("numcell","train", "from"),
            intArrayOf(
                R.id.NUM_CELL,
                R.id.TRAIN_CELL,
                R.id.FROM_CELL,
            )
        )
        list.adapter = mSchedule

        Log.d("blue", blue.toString())
        Log.d("green", green.toString())
        Log.d("red", red.toString())
        Log.d("yellow", yellow.toString())
    }
}