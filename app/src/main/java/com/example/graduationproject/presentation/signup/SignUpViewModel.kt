package com.example.graduationproject.presentation.signup

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduationproject.domain.usecase.SignInUseCase
import com.example.graduationproject.domain.usecase.SignUpUseCase
import com.example.graduationproject.domain.util.EventDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    context: Application,
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
) :
    AndroidViewModel(context) {

    private val _viewState = MutableStateFlow<SignUpViewState>(SignUpViewState.Idle)
    val viewState = _viewState.asStateFlow()

    private val _signUpFailure = MutableSharedFlow<SignUpUseCase.SignUpFailure?>()
    val signUpFailure: SharedFlow<SignUpUseCase.SignUpFailure?> = _signUpFailure.asSharedFlow()

    fun onSignUpButtonClicked(identityValue: String, password: String, username: String, confirmPassword: String) {
        viewModelScope.launch {
            signUpUseCase(SignUpUseCase.Params(identityValue, password, username, confirmPassword))
                .onStart { _viewState.value = SignUpViewState.Loading }
                .catch { exception ->

                    Log.d("SignUpViewModel", "Exception $exception")
                    _viewState.value = SignUpViewState.Failure(
                        exception.localizedMessage ?: "Something went wrong"
                    )
                }
                .collect { event ->
                    Log.d("SignUpViewModel", "Event: $event")
                    when (event) {
                        is EventDomain.Success -> {
                            signIn(identityValue, password)
                        }

                        is EventDomain.Failure -> {
                            _viewState.value = SignUpViewState.Failure("Something went wrong")
                            _signUpFailure.emit(event.failure)
                        }
                    }
                }
        }
    }


    private suspend fun signIn(identityValue: String, password: String) {
        viewModelScope.launch {
            signInUseCase(SignInUseCase.Params(identityValue, password))
                .onStart { _viewState.value = SignUpViewState.Loading }
                .catch {
                    _viewState.value =
                        SignUpViewState.Failure(it.message ?: "Something went wrong.")
                }
                .collect { _ ->
                    _viewState.value = SignUpViewState.Success
                    Log.d("SignUpViewModel", "SignIn $viewState.value")
                }
        }
    }
}
