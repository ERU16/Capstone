const express = require('express');
const { getUserById, updateUserById, deleteUserById } = require('../controllers/userController');
const authenticateToken = require('../middleware/authMiddleware');
const router = express.Router();

router.post('/register', registerUser);
router.post('/login', loginUser);
router.get('/:userId', authenticateToken, getUserById);
router.put('/:userId', authenticateToken, updateUserById);
router.delete('/:userId', authenticateToken, deleteUserById);

module.exports = router;
