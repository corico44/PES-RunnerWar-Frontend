package com.runnerwar.ui.user_leaderboard

import android.os.Bundle
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
import com.runnerwar.Model.UserLeaderboards
import com.runnerwar.R
import com.runnerwar.Repositories.UserRepository
import com.runnerwar.util.Language
import com.runnerwar.util.Session
import kotlinx.android.synthetic.main.fragment_team_leaderboard.*

class UserLeaderboardFragment : Fragment() {

    private lateinit var userLeaderboardViewModel: UserLeaderboardViewModel
    private var listView: ListView? = null
    private var listUsers : List<UserLeaderboards>? = null

    override fun onStart() {
        super.onStart()
        //openPopUpDailyLogin()
        // use arrayadapter and define an array


        userLeaderboardViewModel.leaderboardsUsers()

        userLeaderboardViewModel.responseUsers.observe(this@UserLeaderboardFragment, Observer {
            listUsers = it
            /*MostraValors()*/
            AfegirValors()
        })

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Ini Cuenta viewModel
        val userDao = UserDataBase.getDataBase(activity?.application!!).userDao()
        val repository = UserRepository(userDao, Session.getIdUsuario())
        val viewModelFactory = UserViewModelFactory(repository,7)
        userLeaderboardViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(UserLeaderboardViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_user_leaderboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        val titulo: TextView = root.findViewById(R.id.textView15)
        val columnas: TextView = root.findViewById(R.id.textView19)
        if(Language.idioma.equals("castellano")){
            titulo.text = "CLASIFICACION USUARIOS!"
            columnas.text = "Posicion         Nombre usuario         Honor"
        }
        else if(Language.idioma.equals("ingles")){
            titulo.text = "USER   LEADERBOARD!"
            columnas.text = "Position            Username            Honor"
        }
        return root
    }
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
}