package com.example.runnerwar.ui.buscarCuenta


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.runnerwar.Model.SearchUser
import com.example.runnerwar.R
import kotlinx.android.synthetic.main.fragment_search.*



class SearchFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchViewModel=
            ViewModelProviders.of(this).get(SearchViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        boton_searchuser.setOnClickListener{
            searchViewModel.searchUser(SearchUser(SearchUser.text.toString()))
        }

        searchViewModel.userSearched.observe(this@SearchFragment, Observer {
            UserRetornat.setVisibility(View.VISIBLE)
            FactionSUser.setVisibility(View.VISIBLE)

            UserRetornat.text = it.accountname
            FactionSUser.text = it.faction

        })
    }

}