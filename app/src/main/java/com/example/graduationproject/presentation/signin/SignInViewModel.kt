package com.example.graduationproject.presentation.signin

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.graduationproject.App
import com.example.graduationproject.data.remote.repository.SessionRepositoryImpl
import com.example.graduationproject.domain.usecase.DeviceRegistrationUseCase
import com.example.graduationproject.domain.usecase.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    context: Application,
    private val signInUseCase: SignInUseCase,
    private val deviceRegistrationUseCase: DeviceRegistrationUseCase
) :
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

    fun registerDevice(userId: String, registrationId: String?) {
        viewModelScope.launch {
            runCatching {
                deviceRegistrationUseCase(DeviceRegistrationUseCase.Params(userId, registrationId))
            }.onSuccess {
                Log.d("SignInViewModel", "Device register successfully")
            }.onFailure { e ->
                Log.e("SignInViewModel", "Error register device: ${e.message}")
            }
        }
    }
}




