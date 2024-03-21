package com.example.graduationproject.presentation.signup

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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

    private lateinit var usernameErrorNotification: TextView
    private lateinit var userIdentityErrorNotification: TextView
    private lateinit var passwordErrorNotification: TextView
    private lateinit var confirmPasswordErrorNotification: TextView

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
    }

    private fun initViews() {
        signUpAction = binding.signUpContainer.signUpAction
        userIdentity = binding.signUpContainer.signUpContent
        username = binding.signUpContainer.userNameContent
        userPassword = binding.signUpContainer.createPasswordContent
        userConfirmPassword = binding.signUpContainer.confirmPasswordContent

        initErrorViews()
    }

    private fun initErrorViews() {
        usernameErrorNotification = binding.signUpContainer.invalidUsernameView
        userIdentityErrorNotification = binding.signUpContainer.invalidUserIdentityView
        passwordErrorNotification = binding.signUpContainer.passwordRequirementsView
        confirmPasswordErrorNotification = binding.signUpContainer.invalidPasswordView
    }

    private fun setUpViews() {
        signUpAction.setOnClickListener {
            clearErrorNotifications()

            viewModel.onSignUpButtonClicked(
                identityValue = userIdentity.text.toString(),
                password = userPassword.text.toString(),
                username = username.text.toString(),
                confirmPassword = userConfirmPassword.text.toString()
            )
        }
    }

    private fun clearErrorNotifications() {
        usernameErrorNotification.visibility = View.GONE
        userIdentityErrorNotification.visibility = View.GONE
        confirmPasswordErrorNotification.visibility = View.GONE
        passwordErrorNotification.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.black_text_color
            )
        )
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                Log.d("SignUpViewFragment", "start handling exception")
                viewModel.signUpFailure.collect { failure ->
                    failure?.let { handleSignUpFailure(failure) }
                    Log.d("SignUpViewFragment", "end handling exception")
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                Log.d("SignUpViewFragment", "startVM")
                viewModel.viewState.collect {
                    when (it) {
                        is SignUpViewState.Success -> {
                            Log.d("SignUpViewFragment", "success")
                            moveToHomeScreen()
                        }

                        is SignUpViewState.Loading -> {
                            Log.d("SignUpViewFragment", "loading")
                            signUpAction.isEnabled = false
                        }

                        is SignUpViewState.Failure -> {
                            Log.d("SignUpViewFragment", "failure")
                            signUpAction.isEnabled = true
                        }

                        is SignUpViewState.Idle -> {}
                    }
                }
            }
        }
    }


    private fun handleSignUpFailure(failure: SignUpUseCase.SignUpFailure) {
        when (failure) {
            is SignUpUseCase.SignUpFailure.UsernameExistFailure -> {
                showUsernameExistFailure()
            }

            is SignUpUseCase.SignUpFailure.UserIdentityExistFailure -> {
                showUserIdentityExistFailure()
            }

            is SignUpUseCase.SignUpFailure.UserIdentityFailure -> {
                showUserIdentityFailure()
            }

            is SignUpUseCase.SignUpFailure.PasswordFailure -> {
                showPasswordFailure()
            }

            is SignUpUseCase.SignUpFailure.ConfirmPasswordFailure -> {
                showConfirmPasswordFailure()
            }

            is SignUpUseCase.SignUpFailure.EmptyUsernameFailure -> {
                showEmptyUsernameFailure()
            }

            is SignUpUseCase.SignUpFailure.EmptyIdentityFailure -> {
                showEmptyIdentityFailure()
            }

            is SignUpUseCase.SignUpFailure.EmptyConfirmPasswordFailure -> {
                showEmptyConfirmPasswordFailure()
            }
        }
    }

    private fun showUsernameExistFailure() {
        usernameErrorNotification.setText(R.string.create_username_dialog_invalid_username)
        usernameErrorNotification.visibility = View.VISIBLE
    }

    private fun showUserIdentityExistFailure() {
        userIdentityErrorNotification.setText(R.string.sign_up_exist_user_identity)
        userIdentityErrorNotification.visibility = View.VISIBLE
    }

    private fun showUserIdentityFailure() {
        userIdentityErrorNotification.setText(R.string.sign_up_invalid_user_identity)
        userIdentityErrorNotification.visibility = View.VISIBLE
    }

    private fun showPasswordFailure() {
        passwordErrorNotification.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.error_text_color_red
            )
        )
    }

    private fun showConfirmPasswordFailure() {
        confirmPasswordErrorNotification.setText(R.string.sign_up_invalid_password)
        confirmPasswordErrorNotification.visibility = View.VISIBLE
    }

    private fun showEmptyUsernameFailure() {
        usernameErrorNotification.setText(R.string.sign_up_empty_field)
        usernameErrorNotification.visibility = View.VISIBLE
    }

    private fun showEmptyIdentityFailure() {
        userIdentityErrorNotification.setText(R.string.sign_up_empty_field)
        userIdentityErrorNotification.visibility = View.VISIBLE
    }

    private fun showEmptyConfirmPasswordFailure() {
        confirmPasswordErrorNotification.setText(R.string.sign_up_empty_field)
        confirmPasswordErrorNotification.visibility = View.VISIBLE
    }


    private fun moveToHomeScreen() {
        val action = SignUpViewFragmentDirections.actionSignUpViewFragmentToHomeActivity()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}