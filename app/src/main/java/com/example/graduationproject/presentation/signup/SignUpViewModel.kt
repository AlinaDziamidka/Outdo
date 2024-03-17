package com.example.graduationproject.presentation.signup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.graduationproject.domain.usecase.SignInUseCase
import com.example.graduationproject.domain.usecase.SignUpUseCase
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

    private val _signUpException = MutableSharedFlow<SignUpUseCase.SignUpException?>()
    val signUpException: SharedFlow<SignUpUseCase.SignUpException?> = _signUpException.asSharedFlow()

    fun onSignUpButtonClicked(identityValue: String, password: String, username: String) {
        viewModelScope.launch {
            signUpUseCase(SignUpUseCase.Params(identityValue, password, username))
                .onStart { _viewState.value = SignUpViewState.Loading }
                .catch { exception ->
                    _signUpException.emit(exception as? SignUpUseCase.SignUpException)
                    _viewState.value = SignUpViewState.Failure("Something went wrong.")
                }
                .collect { _ ->
                    signIn(identityValue, password)
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
                }
        }
    }
}
