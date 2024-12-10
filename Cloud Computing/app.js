const express = require('express');
const cors = require("cors");
const path = require("path");
const authRoutes = require('./routes/authRoutes');
const userRoutes = require('./routes/userRoutes');
const animalsRoutes = require('./routes/animalRoutes');
const quizRoutes = require('./routes/quizRoutes');
require('dotenv').config();

const app = express();
app.use(cors());
// Middleware untuk parsing JSON
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

app.use(express.static(path.join(__dirname, 'uploads')));

// Rute AuthRoutes
app.use('/api/auth', authRoutes);
app.use('/api/users', userRoutes);
app.use("/api/animals", animalsRoutes);
app.use("/api/quizzes", quizRoutes);


const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});