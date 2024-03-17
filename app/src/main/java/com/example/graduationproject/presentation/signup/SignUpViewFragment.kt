package com.example.graduationproject.presentation.signup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.graduationproject.R
import com.example.graduationproject.databinding.FragmentSignUpBinding
import com.example.graduationproject.domain.usecase.SignUpUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpViewFragment : Fragment() {

    private val viewModel: SignUpViewModel by viewModels()
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
        setUpViews()
        observeEvents()
//        observeSignUpException()
    }

//    private fun observeSignUpException() {
//        lifecycleScope.launch {
//            viewModel.signUpException.collect { signUpException ->
//                signUpException?.let { handleSignUpException(signUpException) }
//            }
//        }
//    }

    private fun initViews() {
        signUpAction = binding.signUpContainer.signUpAction
        userIdentity = binding.signUpContainer.signUpContent
        username = binding.signUpContainer.userNameContent
        userPassword = binding.signUpContainer.createPasswordContent
        userConfirmPassword = binding.signUpContainer.confirmPasswordContent
    }

    private fun setUpViews() {
        signUpAction.setOnClickListener {
//            if (userPassword.text.toString() !== userConfirmPassword.text.toString()) {
//                showConfirmPasswordFailure()
//            }
            viewModel.onSignUpButtonClicked(
                identityValue = userIdentity.text.toString(),
                password = userPassword.text.toString(),
                username = username.text.toString()
            )
        }
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    when (it) {
                        is SignUpViewState.Success -> {
                            moveToHomeScreen()
                        }

                        is SignUpViewState.Loading -> {
                            signUpAction.isEnabled = false
                        }

                        is SignUpViewState.Failure -> {
                            signUpAction.isEnabled = true

                            viewModel.signUpException.collect { signUpException ->
                                signUpException?.let { handleSignUpException(signUpException) }
                            }
                        }

                        is SignUpViewState.Idle -> {}
                    }
                }
            }
        }
    }

    private fun showConfirmPasswordFailure() {
        binding.signUpContainer.invalidPasswordView.visibility = View.VISIBLE
    }

    private fun handleSignUpException(exception: SignUpUseCase.SignUpException) {
        when (exception) {
            is SignUpUseCase.SignUpException.UsernameException -> {
                showUsernameFailure()
            }

            is SignUpUseCase.SignUpException.UserIdentityException -> {
                showUserIdentityFailure()
            }

            is SignUpUseCase.SignUpException.PasswordException -> {
                showPasswordFailure()
            }

            else -> {
            }
        }
    }

    private fun showPasswordFailure() {
        binding.signUpContainer.passwordRequirementsView.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.error_text_color_red
            )
        )
    }

    private fun showUserIdentityFailure() {
        binding.signUpContainer.invalidUserIdentityView.visibility = View.VISIBLE
    }

    private fun showUsernameFailure() {
        binding.signUpContainer.invalidUsernameView.visibility = View.VISIBLE
    }

    private fun moveToHomeScreen() {
        val action = SignUpViewFragmentDirections.actionSignUpViewFragmentToHomeActivity()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}