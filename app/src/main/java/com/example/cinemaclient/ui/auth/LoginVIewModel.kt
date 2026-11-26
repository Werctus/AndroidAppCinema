package com.example.cinemaclient.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemaclient.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LoginUiState {
    object Idle : LoginUiState
    object Loading : LoginUiState
    data class Success(val token: String) : LoginUiState
    data class Error(val message: String) : LoginUiState
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    init {
        checkExistingSession()
    }

    private fun checkExistingSession() {
        viewModelScope.launch {
            val token = authRepository.getToken()
            if (!token.isNullOrBlank()) {
                _loginState.value = LoginUiState.Success(token)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading

            authRepository.login(email.trim(), password)
                .onSuccess { response ->
                    _loginState.value = LoginUiState.Success(response.token)
                }
                .onFailure { exception ->
                    val errorMsg = exception.message ?: "Неверный email или пароль"
                    _loginState.value = LoginUiState.Error(errorMsg)
                }
        }
    }

    fun register(email: String, password: String, firstName: String?, lastName: String?) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading

            authRepository.register(
                email = email.trim(),
                password = password,
                firstName = firstName?.trim()?.takeIf { it.isNotEmpty() },
                lastName = lastName?.trim()?.takeIf { it.isNotEmpty() }
            ).onSuccess {
                // После успешной регистрации сразу выполняем вход
                login(email, password)
            }.onFailure { exception ->
                val errorMsg = exception.message ?: "Ошибка при регистрации"
                _loginState.value = LoginUiState.Error(errorMsg)
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginUiState.Idle
    }
}
