package com.example.myprofileapp.viewmodel

import com.example.myprofileapp.data.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class ProfileViewModel {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun toggleDarkMode(enabled: Boolean) {
        _uiState.update { it.copy(isDarkMode = enabled) }
    }

    fun setEditing(editing: Boolean) {
        _uiState.update { it.copy(isEditing = editing, tempName = it.name, tempBio = it.bio) }
    }

    fun updateTempName(newName: String) { _uiState.update { it.copy(tempName = newName) } }
    fun updateTempBio(newBio: String) { _uiState.update { it.copy(tempBio = newBio) } }

    fun saveProfile() {
        _uiState.update { it.copy(name = it.tempName, bio = it.tempBio, isEditing = false) }
    }
}