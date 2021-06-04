package com.runnerwar.ui.buscarCuenta


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.runnerwar.Data.User.UserDataBase
import com.runnerwar.Factories.UserViewModelFactory
import com.runnerwar.Model.Codi
import com.runnerwar.Model.Friendship
import com.runnerwar.Model.SearchUser
import com.runnerwar.R
import com.runnerwar.Repositories.UserRepository
import com.runnerwar.util.Language
import com.runnerwar.util.Session
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var loggedUser : String? = activity?.intent?.extras?.getString("email")

        //Ini Cuenta viewModel
        val userDao = UserDataBase.getDataBase(activity?.application!!).userDao()
        val repository = UserRepository(userDao, loggedUser.toString())
        val viewModelFactory = UserViewModelFactory(repository,3)
        searchViewModel=
            ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SearchUser.setEnabled(true)
        var friends: Friendship = Friendship("null", "null")

        if(Language.idioma.equals("castellano")){
            boton_searchuser.setText("Buscar")
            SearchUser.setHint("Nombre cuenta")
        }

        else if(Language.idioma.equals("ingles")){
            boton_searchuser.setText("Search")
            SearchUser.setHint("Account name")
        }

        boton_add_friend.setVisibility(View.INVISIBLE)
        boton_delete_friend.setVisibility(View.INVISIBLE)

        boton_searchuser.setOnClickListener{
            searchViewModel.searchUser(SearchUser(SearchUser.text.toString()))
        }

        boton_add_friend.setOnClickListener{
            searchViewModel.addFriend(friends)
        }

        boton_delete_friend.setOnClickListener{
            searchViewModel.deleteFriend(friends)
        }

        searchViewModel.userSearched.observe(this@SearchFragment, Observer {
            if (it._id != null) {
                UserRetornat.setVisibility(View.VISIBLE)
                FactionSUser.setVisibility(View.VISIBLE)

                friends = Friendship(Session.getIdUsuario(), it._id)
                searchViewModel.searchFriend(friends)
                UserRetornat.text = it.accountname
                FactionSUser.text = it.faction
            }
            else
                if(Language.idioma.equals("castellano")){
                    Toast.makeText(activity?.applicationContext, "El usuario no existe", Toast.LENGTH_SHORT).show()

                }
                else if(Language.idioma.equals("ingles")){
                    Toast.makeText(activity?.applicationContext, "User doesn't exist", Toast.LENGTH_SHORT).show()
                }
        })

        searchViewModel.responseSearch.observe(this@SearchFragment, Observer { response ->

            if (response.isSuccessful){
                val data: Codi? = response.body()
                if (data != null) {
                    if (data.codi == 200)
                        boton_delete_friend.setVisibility(View.VISIBLE)
                    else if (data.codi == 500)
                        boton_add_friend.setVisibility(View.VISIBLE)
                }
            }
        })

        searchViewModel.responseAddFriend.observe(this@SearchFragment, Observer { response ->

            if (response.isSuccessful){
                val data: Codi? = response.body()
                if (data != null) {
                    if(Language.idioma.equals("castellano")){
                        Toast.makeText(activity?.applicationContext, "Amigo añadido", Toast.LENGTH_SHORT).show()
                    }
                    else if(Language.idioma.equals("ingles")){
                        Toast.makeText(activity?.applicationContext, "Friend added", Toast.LENGTH_SHORT).show()
                    }
                    boton_add_friend.setVisibility(View.INVISIBLE)
                    boton_delete_friend.setVisibility(View.VISIBLE)
                }
            }
        })

        searchViewModel.responseDeleteFriend.observe(this@SearchFragment, Observer { response ->

            if (response.isSuccessful){
                val data: Codi? = response.body()
                if (data != null) {
                    if(Language.idioma.equals("castellano")){
                        Toast.makeText(activity?.applicationContext, "Amigo eliminado", Toast.LENGTH_SHORT).show()
                    }
                    else if(Language.idioma.equals("ingles")){
                        Toast.makeText(activity?.applicationContext, "Friend deleted", Toast.LENGTH_SHORT).show()
                    }
                    boton_add_friend.setVisibility(View.VISIBLE)
                    boton_delete_friend.setVisibility(View.INVISIBLE)
                }
            }
        })
    }

}