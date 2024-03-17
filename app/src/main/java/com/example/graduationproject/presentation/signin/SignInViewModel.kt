package com.example.graduationproject.presentation.signin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.graduationproject.App
import com.example.graduationproject.data.repository.SessionRepositoryImpl
import com.example.graduationproject.domain.usecase.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(context: Application, private val signInUseCase: SignInUseCase) :
    AndroidViewModel(context) {

    private val _viewState = MutableStateFlow<SignInViewState>(SignInViewState.Idle)
    val viewState = _viewState.asStateFlow()

    fun onSignInButtonClicked(identityValue: String, password: String) {
            viewModelScope.launch {
                signInUseCase(SignInUseCase.Params(identityValue, password))
                    .onStart {
                        _viewState.value = SignInViewState.Loading
                    }.catch {
                        _viewState.value =
                            SignInViewState.Failure(it.message ?: "Something went wrong.")
                    }.collect { _ ->
                        _viewState.value = SignInViewState.Success
                    }
            }
        }
    }




