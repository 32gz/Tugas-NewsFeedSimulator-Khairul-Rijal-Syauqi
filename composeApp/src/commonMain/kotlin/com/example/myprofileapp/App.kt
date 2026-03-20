package com.example.myprofileapp

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myprofileapp.ui.*
import com.example.myprofileapp.viewmodel.ProfileViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val viewModel = remember { ProfileViewModel() }
    val uiState by viewModel.uiState.collectAsState()

    MaterialTheme(colorScheme = if (uiState.isDarkMode) darkColorScheme() else lightColorScheme()) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Profil Saya", fontWeight = FontWeight.Bold) },
                    actions = {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 12.dp)
                        ) {

                            Icon(
                                imageVector = if (uiState.isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = if (uiState.isDarkMode) Color(0xFFFFD700) else Color.Gray
                            )

                            Spacer(modifier = Modifier.width(8.dp))


                            Text(
                                text = if (uiState.isDarkMode) "Gelap" else "Terang",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Switch(
                                checked = uiState.isDarkMode,
                                onCheckedChange = { viewModel.toggleDarkMode(it) }
                            )
                        }
                    }
                )
            }
        ) { pad ->
            Surface(modifier = Modifier.fillMaxSize().padding(pad)) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProfileHeader(uiState.name, uiState.bio)
                    Spacer(modifier = Modifier.height(24.dp))

                    if (uiState.isEditing) {

                        OutlinedTextField(
                            value = uiState.tempName,
                            onValueChange = { viewModel.updateTempName(it) },
                            label = { Text("Nama") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = uiState.tempBio,
                            onValueChange = { viewModel.updateTempBio(it) },
                            label = { Text("Bio") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row {
                            TextButton(onClick = { viewModel.setEditing(false) }) { Text("Batal") }
                            Button(onClick = { viewModel.saveProfile() }) { Text("Simpan") }
                        }
                    } else {

                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                InfoItem(Icons.Default.Email, "Email", uiState.email)
                                InfoItem(Icons.Default.Phone, "Telepon", uiState.phone)
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { viewModel.setEditing(true) },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Edit Profil")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}