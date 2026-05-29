package com.example.myprofileapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myprofileapp.ui.*
import com.example.myprofileapp.viewmodel.ProfileViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val viewModel = androidx.lifecycle.viewmodel.compose.viewModel<ProfileViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    val snackbarHostState = remember { SnackbarHostState() }
    val saveSuccessMessage = "Profil berhasil disimpan!"

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            snackbarHostState.showSnackbar(saveSuccessMessage)
            viewModel.dismissSaveSuccess()
        }
    }

    MaterialTheme(
        colorScheme = if (uiState.isDarkMode) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Profil Saya",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {
                        DarkModeToggle(
                            isDarkMode = uiState.isDarkMode,
                            onToggle = { viewModel.toggleDarkMode(it) }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        shape = MaterialTheme.shapes.medium
                    )
                }
            }
        ) { pad ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(pad),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProfileHeader(
                        name = uiState.name,
                        bio = uiState.bio
                    )

                    if (uiState.isEditing) {
                        ProfileEditForm(
                            name = uiState.tempName,
                            bio = uiState.tempBio,
                            nameError = uiState.nameError,
                            onNameChange = { viewModel.updateTempName(it) },
                            onBioChange = { viewModel.updateTempBio(it) },
                            onSave = { viewModel.saveProfile() },
                            onCancel = { viewModel.setEditing(false) },
                            isSaving = uiState.isSaving,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                    } else {
                        ProfileInfoCard(
                            email = uiState.email,
                            phone = uiState.phone,
                            location = uiState.location,
                            onEditClick = { viewModel.setEditing(true) },
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                    }
                }
            }
        }
    }
}