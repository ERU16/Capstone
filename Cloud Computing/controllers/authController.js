const { db } = require('../config/db');
const { v4: uuidv4 } = require('uuid');

exports.registerUser = async (req, res) => {
    const { name, email, password } = req.body;
    const id = uuidv4().replace(/-/g, '').slice(0, 16);
    const insertedAt = new Date().toISOString();
    const updatedAt = insertedAt;
    try {
        const userRef = db.collection('users').doc(email);
        const doc = await userRef.get();
        if (doc.exists) {
            return res.status(400).json({
                status: 400,
                message: "User already exists",
                error: {
                    details: "The user has registered an account with the same email address",
                }
            });
        }
        await userRef.set({ id, name, email, password, insertedAt, updatedAt });
        return res.status(201).json({
            status: 201,
            message: "User registered successfully",
            data: {
                userId: id,
                name: name,
                email: email
            }
        });
    } catch (error) {
        return res.status(500).json({
            status: 500,
            message: "Internal server error",
            error: {
                details: error.message
            }
        });
    }
};

exports.loginUser = async (req, res) => {
    const { email, password } = req.body;
    try {
        const userRef = db.collection('users').doc(email);
        const doc = await userRef.get();
        if (!doc.exists || doc.data().password !== password) {
            return res.status(400).json({
                status: 400,
                message: "Invalid credentials",
                error: {
                    details: "Authentication failed. Please check your username and password."
                }
            });
        }
        return res.status(200).json({
            status: 200,
            message: "User logged in successfully",
            data: doc.data()
        });
    } catch (error) {
        return res.status(500).json({
            status: 500,
            message: "Internal server error",
            error: {
                details: error.message
            }
        });
    }
};

//Ambil Data Pengguna (GET)
exports.getUserById = async (req, res) => {
    const { userId } = req.params;
    try {
        const userRef = db.collection('users').doc(userId);
        const doc = await userRef.get();
        if (!doc.exists) {
            return res.status(404).json({
                status: 404,
                message: "User not found",
            });
        }
        return res.status(200).json({
            status: 200,
            message: "User retrieved successfully",
            data: doc.data(),
        });
    } catch (error) {
        return res.status(500).json({
            status: 500,
            message: "Internal server error",
            error: { details: error.message },
        });
    }
};

//Perbarui Data Pengguna (PUT)
exports.updateUserById = async (req, res) => {
    const { userId } = req.params;
    const { email, password } = req.body;
    const updatedAt = new Date().toISOString();

    try {
        const userRef = db.collection('users').doc(userId);
        const doc = await userRef.get();
        if (!doc.exists) {
            return res.status(404).json({
                status: 404,
                message: "User not found",
            });
        }
        await userRef.update({ email, password, updatedAt });
        return res.status(200).json({
            status: 200,
            message: "User updated successfully",
        });
    } catch (error) {
        return res.status(500).json({
            status: 500,
            message: "Internal server error",
            error: { details: error.message },
        });
    }
};

//Hapus Pengguna (DELETE)
exports.deleteUserById = async (req, res) => {
    const { userId } = req.params;

    try {
        const userRef = db.collection('users').doc(userId);
        const doc = await userRef.get();
        if (!doc.exists) {
            return res.status(404).json({
                status: 404,
                message: "User not found",
            });
        }
        await userRef.delete();
        return res.status(200).json({
            status: 200,
            message: "User deleted successfully",
        });
    } catch (error) {
        return res.status(500).json({
            status: 500,
            message: "Internal server error",
            error: { details: error.message },
        });
    }
};
