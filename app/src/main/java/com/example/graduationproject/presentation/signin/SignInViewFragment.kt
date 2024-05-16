package com.example.graduationproject.presentation.signin

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.graduationproject.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInViewFragment : Fragment() {

    private val viewModel: SignInViewModel by viewModels()
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private lateinit var signInAction: Button
    private lateinit var userIdentity: EditText
    private lateinit var userPassword: EditText

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setUpViews()
        observeEvents()
        signUpAction()
    }

    private fun initViews() {
        signInAction = binding.signInContainer.signInAction
        userIdentity = binding.signInContainer.signInContent
        userPassword = binding.signInContainer.passwordContent
    }

    private fun setUpViews() {
        signInAction.setOnClickListener {
            viewModel.onSignInButtonClicked(
                identityValue = userIdentity.text.toString(),
                password = userPassword.text.toString(),
            )
        }
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    when (it) {
                        is SignInViewState.Success -> {
                            moveToHomeScreen()
                        }

                        is SignInViewState.Loading -> {
                            signInAction.isEnabled = false
                        }

                        is SignInViewState.Failure -> {
                            signInAction.isEnabled = true
                            showFailureNotification()
                        }

                        is SignInViewState.Idle -> {}
                    }
                }
            }
        }
    }

    private fun showFailureNotification() {
        binding.signInContainer.invalidUsernameOrPasswordView.visibility = View.VISIBLE
    }

    private fun moveToHomeScreen() {
        val action = SignInViewFragmentDirections.actionSignInViewFragmentToHomeActivity()
        findNavController().navigate(action)
    }

    private fun signUpAction() {
        binding.signInContainer.signUpView.setOnClickListener {
            moveToSignUpScreen()
        }
    }

    private fun moveToSignUpScreen() {
        val action = SignInViewFragmentDirections.actionSignInViewFragmentToSignUpViewFragment()
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}