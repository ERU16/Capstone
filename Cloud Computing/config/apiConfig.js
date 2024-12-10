const express = require('express');
const { loginUser } = require('../controllers/authController');
const rateLimit = require('express-rate-limit');

const router = express.Router();

const loginLimiter = rateLimit({
    windowMs: 15 * 60 * 1000, 
    max: 5, 
    message: { message: 'Too many login attempts, please try again later' },
});

router.post('/login', loginLimiter, loginUser);

module.exports = router;
