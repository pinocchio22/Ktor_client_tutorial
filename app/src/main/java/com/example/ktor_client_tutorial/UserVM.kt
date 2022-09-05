package com.example.ktor_client_tutorial

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UserVM : ViewModel() {

    val userFlow = MutableStateFlow<List<User>>(listOf())

    init {
        Log.d("TAG", "UserVM() init called")

        viewModelScope.launch {
            // 에러 발생을 잡기 위한 runCatching 블럭
            kotlin.runCatching {
                UserRepo.fetchUsers()
            }.onSuccess {fetchedUsers ->
                Log.d("TAG", "UserVM() onSuccess")
                userFlow.value = fetchedUsers
            }.onFailure {
                Log.d("TAG", "UserVM() onFailure")
                userFlow.value = listOf()
            }
        }
    }
}