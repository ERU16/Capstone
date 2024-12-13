# API Documentation

---

## Table of Contents

- [Authentication](#authentication)
- [User Management](#user-management)
- [Animal Data](#animal-data)
- [Quiz Data](#quiz-animal)

---

## Authentication

### POST `/api/auth/register`

- **Description**: Mendaftarkan pengguna baru ke sistem.
- **Request Body**:

  ```json
  {
    "username": "XXXXX",
    "age": "XXXXX@example.com",
    "profilePicture": "file image"
  }
  ```

- **Response**:

  ```json
  {
    "status": 201,
    "message": "User registered successfully",
    "data": {
      "id": "937e3803016e457d",
      "username": "AAA",
      "age": 20,
      "profilePicture": "uploads/1733487437686_88451924.jpeg",
      "insertedAt": "2024-12-06T12:17:18.557Z",
      "updatedAt": "2024-12-06T12:17:18.557Z"
    }
  }
  ```

- **Response (Error)**:
  ```json
  {
    "status": 400,
    "message": "All fields (username, age, profile picture) are required."
  }
  ```

### POST `/api/auth/login`

- **Description**: Mengotentikasi pengguna melalui login.
- **Request Body**:

  ```json
  {
    "username": "AAA"
  }
  ```

- **Response**:

  ```json
  {
    "status": 200,
    "message": "Login successful.",
    "data": {
      "id": "937e3803016e457d",
      "username": "AAA",
      "age": 20,
      "profilePicture": "uploads/1733487437686_88451924.jpeg",
      "insertedAt": "2024-12-06T12:17:18.557Z",
      "updatedAt": "2024-12-06T12:17:18.557Z"
    }
  }
  ```

- **Response (Error)**:
  ```json
  {
    "status": 400,
    "message": "Invalid credentials",
    "error": { "details": "Username is required for login" }
  }
  ```

## User Management

### GET `/api/users/:username`

- **Description**: Mendapatkan profil pengguna berdasarkan ID.
- **Response**:
  ```json
  {
    "status": 200,
    "message": "User retrieved successfully",
    "data": {
      "id": "937e3803016e457d",
      "username": "AAA",
      "age": 20,
      "profilePicture": "uploads/1733487437686_88451924.jpeg",
      "insertedAt": "2024-12-06T12:17:18.557Z",
      "updatedAt": "2024-12-06T12:17:18.557Z"
    }
  }
  ```
- **Response (Error)**:
  ```json
  {
    "status": 404,
    "message": "User not found",
    "error": { "details": "The user does not exist in the database." }
  }
  ```

### PUT `/api/users/:username`

- **Description**: Updates profil pengguna berdasarkan ID.
- **Request Body**:

  ```json
  {
    "usernameUpdate": "XXXXX",
    "ageUpdate": "XXXXX@example.com",
    "profilePicture": "file image"
  }
  ```

- **Response**:

  ```json
  {
    "status": 200,
    "message": "User updated successfully",
    "data": {
      "id": "937e3803016e457d",
      "username": "AAA",
      "insertedAt": "2024-12-06T12:17:18.557Z",
      "profilePicture": "/uploads/1733487648907_d3ab05e7.jpeg",
      "age": "20",
      "updatedAt": "2024-12-06T12:20:49.000Z"
    }
  }
  ```

- **Response (Error)**:
  ```json
  {
    "status": 404,
    "message": "User not found",
    "error": { "details": "The user does not exist in the database." }
  }
  ```

### DELETE `/api/users/:username`

- **Description**: Deletes profil pengguna berdasarkan ID.
- **Response**:

  ```json
  { "status": 200, "message": "User deleted successfully" }
  ```

- **Response (Error)**:
  ```json
  {
    "status": 404,
    "message": "User not found",
    "error": { "details": "The user does not exist in the database." }
  }
  ```

## Animal Data

### GET `/api/animals`

- **Description**: Mendapatkan daftar hewan yang tersedia.
- **Response**:

  ```json
  {
    "status": 200,
    "message": "Receive data successfully",
    "data": [
      {
        "name": "Kelelawar",
        "scientific_name": "Chiroptera",
        "description": "Kelelawar adalah satu-satunya mamalia yang bisa terbang! Mereka hidup di berbagai habitat seperti gua, pohon, dan bahkan bangunan kosong. Kebanyakan kelelawar aktif di malam hari (nokturnal) dan memiliki kemampuan luar biasa untuk menggunakan 'sonar alami' yang disebut ekolokasi. Ini membantu mereka menemukan makanan bahkan dalam kegelapan total! Makanan kelelawar bervariasi, ada yang makan buah-buahan (frugivora), serangga (insektivora), dan ada juga yang menghisap nektar bunga. Ukurannya beragam, mulai dari kelelawar kecil sepanjang 6 cm hingga kelelawar besar seperti kelelawar buah dengan sayap sepanjang 1,5 meter."
      },
      {
        "name": "Beruang",
        "scientific_name": "Ursidae",
        "description": "Beruang adalah hewan besar dan kuat yang hidup di hutan, pegunungan, atau wilayah bersalju. Ada banyak jenis beruang, seperti beruang coklat, beruang kutub, dan beruang madu. Beruang dikenal sebagai omnivora; mereka memakan buah, ikan, dan bahkan serangga! Ukuran beruang bisa sangat besar, dengan berat mencapai 600 kilogram untuk beruang kutub. Mereka juga pelari cepat meskipun terlihat besar dan lamban! Beruang kutub misalnya, adalah perenang hebat yang bisa menjelajahi lautan untuk mencari makanan."
      }, ...
    ]
  }
  ```

### GET `/api/animals/:animalName`

- **Description**: Fetches specific animal details by animal name.
- **Response**:
  ```json
  {
    "status": 200,
    "message": "Receive data successfully",
    "data": {
      "name": "Kelelawar",
      "scientific_name": "Chiroptera",
      "description": "Kelelawar adalah satu-satunya mamalia yang bisa terbang! Mereka hidup di berbagai habitat seperti gua, pohon, dan bahkan bangunan kosong. Kebanyakan kelelawar aktif di malam hari (nokturnal) dan memiliki kemampuan luar biasa untuk menggunakan 'sonar alami' yang disebut ekolokasi. Ini membantu mereka menemukan makanan bahkan dalam kegelapan total! ..."
    }
  }
  ```
- **Response (Error)**:
  ```json
  {
    "status": 404,
    "message": "Animal not found",
    "error": { "details": "The animal not found in database." }
  }
  ```

## Quiz Animal

### GET `/api/quizzes/:animalName`

- **Description**: Retrieves spesific quizzes by name animals.
- **Response**:
  ```json
  {
    "status": 200,
    "message": "Quiz animal data successfully",
    "data": {
      "quiz_id": 1,
      "animal_name": "Kelelawar",
      "quiz_question": "Apa makanan utama kelelawar buah?",
      "quiz_options": ["Serangga", "Buah", "Ikan", "Daging"],
      "correct_answer": "Buah",
      "fun_fact": "Kelelawar buah memainkan peran penting dalam ekosistem dengan menyebarkan biji dan membantu penyerbukan tanaman. Mereka adalah makhluk nokturnal yang menggunakan penglihatan dan penciuman yang tajam untuk menemukan buah di malam hari."
    }
  }
  ```
- **Response (Error)**:
  ```json
  {
    "status": 404,
    "message": "Quiz not found",
    "error": { "details": "The quiz animal not found in database." }
  }
  ```

### POST `/api/quizzes/:animalName/verify`

- **Description**: Fetches specific quiz details by animalName.
- **Request Body**:

  ```json
  {
    "answer": "xxxx"
  }
  ```

- **Response (Correct)**:
  ```json
  {
    "status": 200,
    "message": "Correct answer!",
    "data": {
      "correct": true,
      "fun_fact": "Kelelawar buah memainkan peran penting dalam ekosistem dengan menyebarkan biji dan membantu penyerbukan tanaman. Mereka adalah makhluk nokturnal yang menggunakan penglihatan dan penciuman yang tajam untuk menemukan buah di malam hari."
    }
  }
  ```
- **Response (Incorrect)**:
  ```json
  {
    "status": 200,
    "message": "Incorrect answer",
    "data": { "correct": false, "correct_answer": "Buah" }
  }
  ```
- **Response (Error)**:
  ```json
  { "status": 400, "message": "Answer not provided" }
  ```
