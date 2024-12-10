const express = require('express');
const { registerUser, loginUser } = require('../controllers/authController');
const { upload } = require('../middleware/upload');
const router = express.Router();

router.post('/register', upload.single('profilePicture'), registerUser);
router.post('/login', loginUser);

module.exports = router;
