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

// Состояния экрана входа
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

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading

            authRepository.login(email, password)
                .onSuccess { response ->
                    // Успех: передаем токен дальше
                    _loginState.value = LoginUiState.Success(response.token)
                }
                .onFailure { exception ->
                    // Ошибка: передаем сообщение пользователю
                    val errorMsg = exception.message ?: "Неизвестная ошибка при входе"
                    _loginState.value = LoginUiState.Error(errorMsg)
                }
        }
    }

    // Метод для сброса состояния (например, при возврате на экран)
    fun resetState() {
        _loginState.value = LoginUiState.Idle
    }
}