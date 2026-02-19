News Feed Simulator - Kotlin Multiplatform

Proyek ini adalah aplikasi simulator News Feed yang dibangun menggunakan Kotlin Multiplatform (KMP) dan Jetpack Compose. Aplikasi ini mensimulasikan aliran data berita secara asinkron.

Fitur Utama

News Flow Simulation: Aliran data berita otomatis setiap 2 detik menggunakan Flow.

Category Filter: Filter berita berdasarkan kategori Tech atau Sports.

Data Transformation: Transformasi judul berita menjadi format uppercase sebelum ditampilkan.

StateFlow: Penyimpanan jumlah total berita yang dibaca secara reaktif.

Async Coroutines: Pengambilan detail berita secara asinkron tanpa memblokir antarmuka.

Cara Menjalankan

Buka folder proyek menggunakan Android Studio.

Tunggu proses sinkronisasi Gradle hingga selesai.

Pastikan modul composeApp sudah terpilih pada bagian run configuration.

Jalankan aplikasi pada emulator atau perangkat Android yang terhubung.
