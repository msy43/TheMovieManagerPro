package com.msy.themoviemanagerpro.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.msy.themoviemanagerpro.R
import com.msy.themoviemanagerpro.databinding.FragmentLoginScreenBinding
import com.msy.themoviemanagerpro.ui.activity.LoginActivity
import com.msy.themoviemanagerpro.util.Status
import com.msy.themoviemanagerpro.viewmodel.LoginScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginScreen : Fragment() {

    private var _binding: FragmentLoginScreenBinding? = null
    private val loginScreenBinding get() = _binding!!

    lateinit var viewModel: LoginScreenViewModel

    lateinit var mailEditText: TextInputEditText
    lateinit var passwordEditText: TextInputEditText

    lateinit var mailEditTextLayout: TextInputLayout
    lateinit var passwordEditLayout: TextInputLayout


    lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginScreenBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[LoginScreenViewModel::class.java]

        mailEditText = loginScreenBinding.emailTextInput
        passwordEditText = loginScreenBinding.passwordTextInput

        mailEditTextLayout = loginScreenBinding.emailTextInputLayout
        passwordEditLayout = loginScreenBinding.passwordTextInputLayout

        loginButton = loginScreenBinding.loginButton

        subscribeToObservers()

        mailEditText.addTextChangedListener {
            mailEditTextLayout.error = ""
            viewModel.mailCheck(it.toString())
        }

        passwordEditText.addTextChangedListener {
            passwordEditLayout.error = ""
            viewModel.passwordCheck(it.toString())
        }

        loginButton.setOnClickListener {
            viewModel.login(mailEditText.text.toString(), passwordEditText.text.toString())
        }


        return loginScreenBinding.root
    }

    private fun subscribeToObservers() {
        viewModel.loginMessage.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    val message = requireContext().resources.getString(R.string.welcome) + it.message
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    (activity as LoginActivity).toMainActivity()
                }
                Status.ERROR -> {
                    if (it.data.equals("m")) {
                        when(it.message){
                            "invalid" -> mailEditTextLayout.error = requireContext().resources.getString(R.string.email_invalid)
                            "empty" -> mailEditTextLayout.error = requireContext().resources.getString(R.string.enter_mail)
                        }
                    } else if (it.data.equals("p")) {
                        when(it.message){
                            "contains" -> passwordEditLayout.error = requireContext().resources.getString(R.string.password_contains)
                            "length" -> passwordEditLayout.error = requireContext().resources.getString(R.string.password_length)
                            "empty" -> passwordEditLayout.error = requireContext().resources.getString(R.string.enter_password)
                        }
                    }
                }
                Status.LOADING -> {

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}