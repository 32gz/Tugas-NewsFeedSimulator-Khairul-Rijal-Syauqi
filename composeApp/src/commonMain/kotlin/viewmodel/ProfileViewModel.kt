package com.example.myprofileapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofileapp.data.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            _uiState.update { it.copy(isDarkMode = enabled) }
        }
    }

    fun setEditing(editing: Boolean) {
        _uiState.update { it.copy(isEditing = editing, tempName = it.name, tempBio = it.bio) }
    }

    fun updateTempName(newName: String) {
        _uiState.update { it.copy(tempName = newName, nameError = null) }
    }

    fun updateTempBio(newBio: String) {
        _uiState.update { it.copy(tempBio = newBio) }
    }

    fun saveProfile() {
        val currentState = _uiState.value


        if (currentState.tempName.isBlank()) {
            _uiState.update { it.copy(nameError = "Nama tidak boleh kosong") }
            return
        }

        if (currentState.tempName.length < 3) {
            _uiState.update { it.copy(nameError = "Nama minimal 3 karakter") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }


            kotlinx.coroutines.delay(500)

            _uiState.update {
                it.copy(
                    name = it.tempName,
                    bio = it.tempBio,
                    isEditing = false,
                    isSaving = false,
                    saveSuccess = true
                )
            }
        }
    }

    fun dismissSaveSuccess() {
        _uiState.update { it.copy(saveSuccess = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(nameError = null) }
    }
}