package com.example.cinemaclient.util

object Constants {
    // Для эмулятора Android используйте 10.0.2.2 вместо localhost
    // Для реального устройства укажите IP вашего ПК в локальной сети
    const val BASE_URL = "http://10.0.2.2:8080/"

    const val PREFS_NAME = "cinema_prefs"
    const val KEY_AUTH_TOKEN = "auth_token"
    const val KEY_USER_ROLE = "user_role"
}