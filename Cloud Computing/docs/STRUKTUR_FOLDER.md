project-root/
│
├── config/                         # Konfigurasi aplikasi umum
│   ├── db.js                       # Konfigurasi koneksi database
│   ├── cloudStorage.js             # Konfigurasi untuk penyimpanan cloud
│   └── machineLearningConfig.js    # Konfigurasi untuk layanan ML
│   └── authenticationConfig.js     # Untuk konfigurasi autentikasi (misalnya Firebase).
│   └── apiConfig.js                # Jika ada konfigurasi umum API, misalnya pengaturan CORS, rate limiting, dll
│
├── controllers/                    # Logika utama untuk setiap endpoint API
│   ├── animalController.js         # Controller untuk data hewan
│   └── quizController.js           # Controller untuk kuis mini
│
├── models/                         # Model data untuk database
│   ├── Animal.js                   # Model untuk representasi data hewan
│   └── Quiz.js                     # Model untuk data kuis
│
├── routes/                         # Endpoint API untuk data dan fitur aplikasi
│   ├── animalRoutes.js             # Endpoint khusus untuk data hewan
│   └── quizRoutes.js               # Endpoint untuk kuis mini
│
├── middleware/                     # Middleware (contohnya autentikasi)
│   ├── authMiddleware.js           # Middleware untuk autentikasi user
│   └── loggingMiddleware.js        # Middleware untuk pencatatan aktivitas
│
├── services/                       # Layanan yang mengelola fungsi spesifik
│   ├── cloudStorageService.js      # Layanan untuk mengelola penyimpanan cloud
│   ├── machineLearningService.js   # Layanan untuk pemanggilan model ML
│   └── audioService.js             # Layanan untuk memproses audio deskripsi
│
├── machineLearning/                # File dan skrip terkait Machine Learning
│   ├── modelDeployment.js          # Skrip untuk deploy model ML ke cloud
│   └── dataPreprocessing.js        # Skrip preprocessing data
│
├── mobileIntegration/              # Endpoint untuk mobile app, khusus fitur tertentu
│   ├── pushNotifications.js        # Layanan push notification ke app
│   └── syncService.js              # Sinkronisasi data dengan aplikasi mobile
│
├── assets/                         # Jika ada aset statis (gambar default, file audio, dll.) yang perlu diakses secara publik
│
├── utils/                          # Helper functions atau utilities
│   └── helper.js                   # Fungsi utilitas umum
│
├── tests/                          # Unit tests dan integration tests
│   ├── animalController.test.js    # Testing untuk animalController
│   └── quizController.test.js      # Testing untuk quizController
│
├── docs/                           # Dokumentasi proyek
│   └── API_DOCUMENTATION.md        # Dokumentasi untuk API yang dikembangkan
│
├── .env                            # Variabel lingkungan untuk konfigurasi yang sensitif
├── .gitignore                      # File dan folder yang diabaikan oleh Git
├── app.js                          # File utama untuk konfigurasi Express dan middleware global
└── package.json                    # Konfigurasi dependensi npm