package com.example.runnerwar.Model

import com.example.runnerwar.ui.muro.MuroViewModel

class Data {
    private lateinit var _userForm: UserForm
    private lateinit var _userRes: UserResponse


    fun Data(email:String , username: String, password: String) {
        _userForm.username = username
        _userForm.email = email
        _userForm.password
    }


}