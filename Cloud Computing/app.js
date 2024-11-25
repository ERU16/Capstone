const express = require('express');
const connectDB = require('./config/db');
const authRoutes = require('./routes/authRoutes');
const userRoutes = require('./routes/userRoutes');
require('dotenv').config();

const app = express();

// Middleware untuk parsing JSON
app.use(express.json());

// Rute AuthRoutes
app.use('/api/auth', authRoutes);
app.use('/api/users', userRoutes);

module.exports = app;
