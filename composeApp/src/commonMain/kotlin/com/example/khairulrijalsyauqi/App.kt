package com.example.khairulrijalsyauqi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


data class NewsItem(val id: Int, val title: String, val category: String)

data class NewsDisplayModel(
    val id: Int,
    val displayTitle: String,
    val categoryTag: String,
    val detail: String = "Mengambil detail berita..."
)


@Composable
fun getPlatformName(): String = "Android / iOS (KMP)"


class NewsRepository {
    private val rawNews = listOf(
        NewsItem(1, "Kotlin Multiplatform semakin populer", "Tech"),
        NewsItem(2, "Hasil pertandingan Liga Inggris tadi malam", "Sports"),
        NewsItem(3, "Perkembangan terbaru AI di tahun 2025", "Tech"),
        NewsItem(4, "Festival Kuliner Nusantara resmi dibuka", "Food"),
        NewsItem(5, "Tips investasi saham bagi pemula", "Finance")
    )


    fun getNewsStream(): Flow<NewsItem> = flow {
        var index = 0
        while (true) {
            val news = rawNews[index % rawNews.size].copy(id = (1000..9999).random())
            emit(news)
            index++
            delay(2000)
        }
    }

    suspend fun fetchNewsDetail(id: Int): String {
        delay(1500) // Simulasi latency jaringan
        return "Ini adalah konten lengkap untuk berita dengan ID #$id. Konten ini diambil secara asinkron dari server simulasi."
    }
}


@Composable
fun App() {
    val scope = rememberCoroutineScope()
    val repository = remember { NewsRepository() }


    var newsList by remember { mutableStateOf(emptyList<NewsDisplayModel>()) }
    var currentCategory by remember { mutableStateOf("Tech") }


    var readCount by remember { mutableStateOf(0) }


    LaunchedEffect(currentCategory) {
        repository.getNewsStream()

            .filter { it.category == currentCategory }

            .map { news ->
                NewsDisplayModel(
                    id = news.id,
                    displayTitle = "[TERBARU] ${news.title.uppercase()}",
                    categoryTag = "#${news.category.lowercase()}"
                )
            }
            .collect { newNews ->

                newsList = (listOf(newNews) + newsList).take(5)


                scope.launch {
                    val detail = repository.fetchNewsDetail(newNews.id)

                    newsList = newsList.map {
                        if (it.id == newNews.id) it.copy(detail = detail) else it
                    }
                    readCount++
                }
            }
    }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF8F9FA)) {
            Column(modifier = Modifier.padding(16.dp)) {


                ProfileHeader(name = "Khairul Rijal Syauqi", nim = "123140143")

                Spacer(modifier = Modifier.height(16.dp))


                NewsStatsRow(readCount = readCount)

                Spacer(modifier = Modifier.height(12.dp))


                CategorySelector(
                    selected = currentCategory,
                    onCategorySelected = {
                        currentCategory = it
                        newsList = emptyList() // Reset feed saat ganti kategori
                    }
                )

                Text(
                    text = "Streaming berita kategori: $currentCategory...",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )


                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(newsList, key = { it.id }) { item ->
                        NewsCard(item)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileHeader(name: String, nim: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = name, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
            Text(text = "NIM: $nim", style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "Platform: ${getPlatformName()}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun NewsStatsRow(readCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Total Berita Diproses: $readCount",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun CategorySelector(selected: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf("Tech", "Sports", "Food", "Finance")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { cat ->
            FilterChip(
                selected = selected == cat,
                onClick = { onCategorySelected(cat) },
                label = { Text(cat) }
            )
        }
    }
}

@Composable
fun NewsCard(news: NewsDisplayModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = news.categoryTag,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "ID: ${news.id}", fontSize = 10.sp, color = Color.LightGray)
            }

            Text(
                text = news.displayTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(vertical = 6.dp),
                lineHeight = 20.sp
            )

            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = news.detail,
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
        }
    }
}