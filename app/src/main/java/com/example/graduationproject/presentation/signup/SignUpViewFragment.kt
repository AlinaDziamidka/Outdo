package com.example.graduationproject.presentation.signup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.graduationproject.databinding.FragmentSignUpBinding

class SignUpViewFragment : Fragment() {

    private val viewModel: SignUpViewModel by viewModels { SignUpViewModel.SignUpViewModelFactory }
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var userIdentity: EditText
    private lateinit var username: EditText
    private lateinit var userPassword: EditText
    private lateinit var userConfirmPassword: EditText
    private lateinit var signUpAction: Button

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        signUpAction= binding.signUpContainer.signUpAction
        userIdentity = binding.signUpContainer.signUpContent
        username = binding.signUpContainer.userNameContent
        userPassword = binding.signUpContainer.createPasswordContent
        userConfirmPassword = binding.signUpContainer.confirmPasswordContent
    }

    private fun setUpViews() {
        signUpAction.setOnClickListener {
            viewModel.onSignUpButtonClicked(
                identityValue = userIdentity.text.toString(),
                password = userPassword.text.toString(),
            )
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }
}