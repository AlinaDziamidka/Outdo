package com.example.graduationproject.presentation.signin

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.graduationproject.R
import com.example.graduationproject.data.worker.InitialLoadWorker
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
    private lateinit var progressOverlay: FrameLayout
    private var isPasswordVisible = false

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
        observePasswordVisibility()
        observeEvents()
        signUpAction()
    }

    private fun initViews() {
        signInAction = binding.signInContainer.signInAction
        userIdentity = binding.signInContainer.signInContent
        userPassword = binding.signInContainer.passwordContent
        progressOverlay = binding.progressOverlay
    }

    private fun setUpViews() {
        signInAction.setOnClickListener {
            viewModel.onSignInButtonClicked(
                identityValue = userIdentity.text.toString(),
                password = userPassword.text.toString(),
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun observePasswordVisibility() {
        userPassword.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (userPassword.right - userPassword.compoundDrawables[2].bounds.width())) {
                    togglePasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            view.performClick()
            return@setOnTouchListener false
        }
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            userPassword.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            userPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            userPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_invisible, 0)
            userPassword.typeface = Typeface.DEFAULT
        } else {
            userPassword.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            userPassword.transformationMethod = null
            userPassword.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_eye_visible,
                0
            )
            userPassword.typeface = ResourcesCompat.getFont(requireContext(), R.font.gilroy_regular)
        }
        userPassword.setSelection(userPassword.text.length)
        isPasswordVisible = !isPasswordVisible
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    when (it) {
                        is SignInViewState.Success -> {
                            handleOnSuccess()
                        }

                        is SignInViewState.Loading -> {
                            progressOverlay.visibility = View.VISIBLE
                            signInAction.isEnabled = false
                        }

                        is SignInViewState.Failure -> {
                            handleOnFailure()
                        }

                        is SignInViewState.Idle -> {}
                    }
                }
            }
        }
    }

    private fun handleOnSuccess() {
        runWorker()
        registerUserDevice()
        moveToHomeScreen()
        progressOverlay.visibility = View.GONE
    }

    private fun registerUserDevice() {
        viewModel.registerDevice()
    }

    private fun runWorker() {
        val sharedPreferences =
            requireContext().getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("current_user_id", "  ") ?: "  "
        val inputData = workDataOf("USER_ID" to userId)

        val workRequest = OneTimeWorkRequestBuilder<InitialLoadWorker>()
            .setInputData(inputData)
            .build()
        context?.let { WorkManager.getInstance(it).enqueue(workRequest) }
    }

    private fun handleOnFailure() {
        signInAction.isEnabled = true
        progressOverlay.visibility = View.GONE
        showFailureNotification()
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