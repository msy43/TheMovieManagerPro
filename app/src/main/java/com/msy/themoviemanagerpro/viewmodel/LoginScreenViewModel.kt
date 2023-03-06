package com.msy.themoviemanagerpro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.msy.themoviemanagerpro.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(): ViewModel() {

    private var loginMsg = MutableLiveData<Resource<String>>()
    val loginMessage: LiveData<Resource<String>> get() = loginMsg


    fun resetInsertArtMsg() {
        loginMsg = MutableLiveData<Resource<String>>()
    }

    fun login(mail: String, password: String) {
        if (mailCheck(mail) && passwordCheck(password)) {
            loginMsg.postValue(Resource.success(" $mail !", mail))
        }
    }

    fun mailCheck(mail: String): Boolean {
        return if (mail.isNotEmpty()) {
            if (emailValidation(mail)) {
                true
            } else {
                loginMsg.postValue(Resource.error("invalid", "m"))
                false
            }
        } else {
            loginMsg.postValue(Resource.error("empty", "m"))
            false
        }
    }

    fun passwordCheck(password: String): Boolean {
        if (password.isNotEmpty()) {
            if (password.length >= 6) {
                return if (passwordValidation(password)) {
                    true
                } else {
                    loginMsg.postValue(
                        Resource.error(
                            "contains",
                            "p"
                        )
                    )
                    false
                }

            } else {
                loginMsg.postValue(
                    Resource.error(
                       "length",
                        "p"
                    )
                )
                return false
            }

        } else {
            loginMsg.postValue(
                Resource.error(
                    "empty",
                    "p"
                )
            )
            return false
        }
    }


    private fun emailValidation(emailString: String): Boolean {

        val emailPattern: Pattern =
            Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        val emailMatcher: Matcher = emailPattern.matcher(emailString)
        return emailMatcher.matches()
    }

    private fun passwordValidation(passwordString: String): Boolean {

        val passwordPattern: Pattern =
            Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$")
        val passwordMatcher: Matcher = passwordPattern.matcher(passwordString)
        return passwordMatcher.matches()
    }
}