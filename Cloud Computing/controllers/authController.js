const multer = require('multer');
const { db, bucket } = require('../config/db');
const { v4: uuidv4 } = require('uuid');

exports.registerUser = async (req, res) => {
    try {
        // Validasi input
        const { username, age } = req.body;
        const id = uuidv4().replace(/-/g, '').slice(0, 8);

        if (!username || !age) {
            return res.status(400).json({
                status: 400,
                message: "All fields (username, age) are required.",
            });
        }

        const usersRef = db.collection('users');
        const doc = await usersRef.where('username', '==', username).get();
        if (!doc.empty) {
            return res.status(400).json({
                status: 400,
                message: "User already exists",
                error: {
                    details: "The user has registered an account with the same username",
                }
            });
        }

        let profilePictureUrl = null;

        if (req.file) {
            // Jika file diberikan, upload ke bucket GCP
            const file = req.file;
            const bucketFileName = `profile_image-users/${id}-${file.originalname}`;
            const fileUpload = bucket.file(bucketFileName);
            const fileStream = fileUpload.createWriteStream({
                metadata: {
                    contentType: file.mimetype,
                },
            });

            fileStream.end(file.buffer);

            // Tunggu upload selesai
            await new Promise((resolve, reject) => {
                fileStream.on('finish', resolve);
                fileStream.on('error', reject);
            });

            profilePictureUrl = `https://storage.googleapis.com/${bucket.name}/${bucketFileName}`;
        }

        // Simpan data pengguna di Firestore
        const newUser = {
            id: id,
            username,
            age: parseInt(age, 10),
            profilePicture: profilePictureUrl, // Bisa null jika tidak diunggah
            insertedAt: new Date().toISOString(),
            updatedAt: new Date().toISOString(),
        };

        await usersRef.doc(newUser.id).set(newUser);

        return res.status(201).json({
            status: 201,
            message: "User registered successfully",
            data: newUser,
        });
    } catch (error) {
        return res.status(500).json({
            status: 500,
            message: "Failed to register user.",
            error: {
                details: error.message,
            },
        });
    }
};


// exports.registerUser = async (req, res) => {
//     try {
//         // Validasi input
//         const { username, age } = req.body;
//         const id = uuidv4().replace(/-/g, '').slice(0, 16);

//         if (!username || !age) {
//             return res.status(400).json({
//                 status: 400,
//                 message: "All fields (username, age, profile picture) are required.",
//             });
//         }

//         const usersRef = db.collection('users');
//         const doc = await usersRef.where('username', '==', username).get();
//         if (doc.exists) {
//             return res.status(400).json({
//                 status: 400,
//                 message: "User already exists",
//                 error: {
//                     details: "The user has registered an account with the same email address",
//                 }
//             });
//         }

//         // Nama file dari middleware multer
//         const nameProfileImg = `uploads/${req.file.filename}`;

//         // Simpan data pengguna di Firestore
//         const newUser = {
//             id: id,
//             username,
//             age: parseInt(age, 10),
//             profilePicture: nameProfileImg,
//             insertedAt: new Date().toISOString(),
//             updatedAt: new Date().toISOString(),
//         };

//         await usersRef.doc(newUser.id).set(newUser);

//         return res.status(201).json({
//             status: 201,
//             message: "User registered successfully",
//             data: newUser,
//         });
//     } catch (error) {
//         return res.status(500).json({
//             status: 500,
//             message: "Failed to register user.",
//             error: {
//                 details: error.message,
//             },
//         });
//     }
// };


exports.loginUser = async (req, res) => {
    try {
        const { username } = req.body;

        if (!username) {
            return res.status(400).json({
                status: 400,
                message: "Invalid credentials",
                error: {
                    details: "Username is required for login"
                }
            });
        }

        const usersRef = db.collection('users');
        const querySnapshot = await usersRef.where('username', '==', username).get();

        if (querySnapshot.empty) {
            return res.status(404).json({
                status: 404,
                message: "Invalid credentials",
                error: {
                    details: "User not found"
                }
            });
        }

        // Ambil data user
        const userDoc = querySnapshot.docs[0];
        const userData = userDoc.data();

        return res.status(200).json({
            status: 200,
            message: "Login successful.",
            data: userData,
        });
    } catch (error) {
        return res.status(500).json({
            status: 500,
            message: "Failed to login user.",
            error: {
                details: error.message,
            },
        });
    }
};


// module.exports = upload; 

// exports.registerUser = async (req, res) => {
//     const { name, email, password } = req.body;
//     const id = uuidv4().replace(/-/g, '').slice(0, 16);
//     const insertedAt = new Date().toISOString();
//     const updatedAt = insertedAt;
//     try {
//         const userRef = db.collection('users').doc(email);
//         const doc = await userRef.get();
//         if (doc.exists) {
//             return res.status(400).json({
//                 status: 400,
//                 message: "User already exists",
//                 error: {
//                     details: "The user has registered an account with the same email address",
//                 }
//             });
//         }
//         await userRef.set({ id, name, email, password, insertedAt, updatedAt });
//         return res.status(201).json({
//             status: 201,
//             message: "User registered successfully",
//             data: {
//                 userId: id,
//                 name: name,
//                 email: email
//             }
//         });
//     } catch (error) {
//         return res.status(500).json({
//             status: 500,
//             message: "Internal server error",
//             error: {
//                 details: error.message
//             }
//         });
//     }
// };

// exports.loginUser = async (req, res) => {
//     const { email, password } = req.body;
//     try {
//         const userRef = db.collection('users').doc(email);
//         const doc = await userRef.get();
//         if (!doc.exists || doc.data().password !== password) {
//             return res.status(400).json({
//                 status: 400,
//                 message: "Invalid credentials",
//                 error: {
//                     details: "Authentication failed. Please check your username and password."
//                 }
//             });
//         }
//         return res.status(200).json({
//             status: 200,
//             message: "User logged in successfully",
//             data: doc.data()
//         });
//     } catch (error) {
//         return res.status(500).json({
//             status: 500,
//             message: "Internal server error",
//             error: {
//                 details: error.message
//             }
//         });
//     }
// };

