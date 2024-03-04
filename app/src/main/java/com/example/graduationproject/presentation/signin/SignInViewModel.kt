package com.example.graduationproject.presentation.signin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.graduationproject.App
import com.example.graduationproject.Event
import com.example.graduationproject.data.repository.SessionRepositoryImpl
import com.example.graduationproject.domain.usecase.SignInUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SignInViewModel(context: Application, private val signInUseCase: SignInUseCase) :
    AndroidViewModel(context) {

    private val _viewState = MutableStateFlow<SignInViewState>(SignInViewState.Idle)
    val viewState: StateFlow<SignInViewState> = _viewState

    companion object {

        val SignInViewModelFactory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val sessionRepository = SessionRepositoryImpl()
                val signInUseCase = SignInUseCase(sessionRepository)
                return@initializer SignInViewModel(
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App,
                    signInUseCase
                )
            }
        }
    }

    fun onSignInButtonClicked(identityValue: String, password: String) {
        viewModelScope.launch {
            val event = signInUseCase(
                SignInUseCase.Params(identityValue, password)
            )
            when (event) {
                is Event.Success -> {
                    event.data.onStart { _viewState.value = SignInViewState.Loading }
                        .catch {
                            _viewState.value =
                                SignInViewState.Failure(it.message ?: "Something went wrong.")
                        }
                        .collect { _ ->
                            _viewState.value = SignInViewState.Success
                        }
                }

                is Event.Failure -> {
                    _viewState.value = SignInViewState.Failure(event.message)
                }
            }
        }
    }
}

