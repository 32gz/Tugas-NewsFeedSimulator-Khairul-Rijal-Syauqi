package com.example.myprofileapp.data


data class ProfileUiState(
    val name: String = "Khairul Rijal Syauqi",
    val bio: String = "Mahasiswa Informatika - Developer Kotlin",
    val email: String = "khairulsyauqi1@gmail.com",
    val phone: String = "+62 813 8568 0425",
    val location: String = "Lampung, Indonesia",
    val isDarkMode: Boolean = false,
    val isEditing: Boolean = false,
    val tempName: String = "",
    val tempBio: String = ""
)