# Animal Peek: Final Capstone Project

**Team ID:** C242-PS209

**Theme:** Improving the Quality of Education  

---

## Team Members

| Role      | ID              | Name                               | Institution                                    | Status   |
|-----------|-----------------|------------------------------------|-----------------------------------------------|----------|
| Machine Learning (ML) | M297B4KY0783 | Bagas Duta Prasetya              | Universitas Pembangunan Nasional “Veteran” Yogyakarta | Active |
| Machine Learning (ML) | M297B4KY1586 | Galvin Suryo Asmoro              | Universitas Pembangunan Nasional “Veteran” Yogyakarta | Active |
| Machine Learning (ML) | M245B4KY4217 | Steven Graciano Immanuel Cahyono | Universitas Kristen Satya Wacana              | Active |
| Cloud Computing (CC)   | C179B4KY1052 | Destian Aldi Nugraha             | Universitas Ahmad Dahlan                      | Active |
| Cloud Computing (CC)   | C179B4KY4590 | Zahri Ramadhani                  | Universitas Ahmad Dahlan                      | Active |
| Mobile Development (MD)| A179B4KY2860 | Muhammad Gustika Chafidh 'Alim   | Universitas Ahmad Dahlan                      | Active |
| Mobile Development (MD)| A007B4KY2961 | Muhammad Naufal Maulana          | Universitas Dian Nuswantoro                   | Active |

---

## Project Overview

**Animal Peek** is an interactive app that uses machine learning to teach children about animals through visuals, audio, fun facts, and quizzes. With a user-friendly design, kids can identify animals using a camera, learn about their traits, and enjoy engaging quizzes. The app promotes learning, curiosity, and wildlife conservation awarenes

---

## Machine Learning Learning Path

### Steps to Generate the Model
1. **Get the API Token**  
   - Visit [Kaggle](https://www.kaggle.com) and generate an API token.
   - Upload the token to your Google Colab environment.

2. **Download the Dataset**  
   - Connect Google Colab to Kaggle using the API token.  
   - Download the zip dataset from Kaggle.

3. **Setup TensorFlow**  
   - Install TensorFlow 2.15.  
   - Restart the runtime before proceeding.

4. **Train the Model**  
   - Run all remaining cells in the notebook to train the model.  
   - Save the trained model for deployment.

---

## Featured Technologies
- **TensorFlow**
- **TensorFlow Lite**
- **Other Libraries**: **OpenCV**, **numpy**, **pandas**, **matplotlib**, **seaborn**, **sklearn** for image processing, data manipulation, and model evaluation.


---

## Dataset

1. **Public Dataset**  
   Original dataset used for the project:  
   [Animal Image Dataset - 90 Different Animals](https://www.kaggle.com/datasets/iamsouravbanerjee/animal-image-dataset-90-different-animals/data)

2. **Final Dataset**  
   Refined dataset for the project:  
   [Animal Peek Dataset](https://www.kaggle.com/datasets/imanuelsteven/animal-peek-dataset)

## Model And Accuracy
1. **Train-Val Accuracy & Train-Val Loss**  
   ![Accuracy and Loss](https://github.com/ERU16/Capstone/blob/20d445469563831dfc92f13464464a870813272c/Machine%20Learning/Accuracy%20And%20Loss.png)

1. **Model Evaluate on the Test Set**
    ![Model Evaluate](https://github.com/ERU16/Capstone/blob/23695135dc46e4334d48c0656641b82b01c3d22b/Machine%20Learning/Evaluate.png)

---

## Cloud Computing Path

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

   ---

 ## Mobile Development Learning Path
   
 ### Workflow  
 1. **Create UI Design**
  - Open **Figma**(https://www.figma.com/)
  - Create/Login to existed account
  - Create the design for every page
  - Create Mockup and prototype (*to make detailed visual representation*)

       
  ![Halaman Utama](https://github.com/user-attachments/assets/eab072a5-5720-49d5-87a3-e61acdcbd596)
  ![After Scan Hewan](https://github.com/user-attachments/assets/560d95ef-ef3e-4128-ab97-b451b13f16e0)
        
 2. **Implement Design on Code**
  - Develop using **Kotlin** Programming Language in **Android Studio**
  -
  
 3. **Integrated API to get Animal data (Animal Name, Description, Sound, User Profile)**
  -   
 
 4. **Implement Machine Learning Model**
  -
  - 
 5. **Deploy into .apk source**
  - Build app in Android Studio
  - APK File
  (https://drive.google.com/drive/folders/1PnOShx919TFS-08zI9u6Gl5Cj0Y8B8ro?usp=sharing)
  
  ![WhatsApp Image 2024-12-13 at 13 57 38](https://github.com/user-attachments/assets/425011f7-b889-4956-91a4-fbad665627dd)
  


 ---
 ### Tools

 - **Android Studio** : Kotlin
 - **Figma**          : UI/UX Design, Mockup, Prototype.





