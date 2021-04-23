package com.example.runnerwar

import android.util.Patterns
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.security.MessageDigest

class LoginViewTest {

    fun hashString(input: String, algorithm: String): String    {
        return MessageDigest.getInstance(algorithm)
            .digest(input.toByteArray())
            .fold("", { str, it -> str + "%02x".format(it) })
    }


    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    // A placeholder password validation check
    private fun isUserNameValid(username: String): Boolean {
        return (username.length in 5..10)
    }


    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun contieneMayuscula(a: String): Boolean {
        for(i in 0..(a.length-1)) {
            if (a[i] in 'A'..'Z') {
                return true
            }
        }
        return false
    }

    private fun contieneMinuscula(a: String): Boolean {
        for(i in 0..(a.length-1)) {
            if (a[i] in 'a'..'z') {
                return true
            }
        }
        return false
    }

    // A placeholder password validation check
    fun isPasswordValid(password: String): Boolean {
        if(password.contains('!') or password.contains('_') or password.contains('/')){
            return false
        }
        return (password.length in 6..12 && contieneMayuscula(password) && contieneMinuscula(password))
    }
}

class LoginViewUniTest {

    private  var loginViewModel: LoginViewTest = LoginViewTest()


    @Test
    fun encrypt_password(){
        val pass : String =  loginViewModel.hashString("1234", "SHA-256")
        assertEquals("03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4", pass)
    }

    @Test
    fun correct_format_password(){
        var correct =loginViewModel.isPasswordValid("Exam1234")
        assertEquals(correct, true)
    }

    @Test
    fun incorrect_format_password(){
        var correct =loginViewModel.isPasswordValid("exam1234")
        assertEquals(correct, false)
    }

    /*@Test
    fun correct_format_email(){
        var correct =loginViewModel.isEmailValid("example@default.com")
        assertEquals(correct, true)
    }

    @Test
    fun incorrect_format_email(){
        var correct =loginViewModel.isEmailValid("exampledefault.com")
        assertEquals(correct, false)
    }*/


}