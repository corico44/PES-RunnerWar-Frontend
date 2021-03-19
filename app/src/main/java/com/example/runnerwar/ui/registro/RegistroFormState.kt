package com.example.runnerwar.ui.registro

data class RegistroFormState(
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val isDataValid: Boolean = false
)
