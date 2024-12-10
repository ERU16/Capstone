const express = require('express');
const multer = require('multer');
const { upload } = require('../middleware/upload');
const { getUserByUsername, updateUserByUsername, deleteUserByUsername } = require('../controllers/userController');
const router = express.Router();

router.get('/:username', getUserByUsername);
router.put('/:username', upload.single('profilePicture'), updateUserByUsername);
router.delete('/:username', deleteUserByUsername);

module.exports = router;
