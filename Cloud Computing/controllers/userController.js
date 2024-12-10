const { db, bucket } = require('../config/db');

exports.getUserByUsername = async (req, res) => {
  const { username } = req.params;
  try {
    const usersRef = db.collection('users');
    const querySnapshot = await usersRef.where('username', '==', username).get();

    if (querySnapshot.empty) {
      return res.status(404).json({
        status: 404,
        message: "User not found",
        error: {
          details: "The user does not exist in the database.",
        },
      });
    }

    const user = querySnapshot.docs[0].data();
    return res.status(200).json({
      status: 200,
      message: "User retrieved successfully",
      data: user,
    });
  } catch (error) {
    return res.status(500).json({
      status: 500,
      message: "Failed to retrieve user",
      error: {
        details: error.message,
      },
    });
  }
};

exports.updateUserByUsername = async (req, res) => {
  const { username } = req.params;
  const { usernameUpdate, ageUpdate } = req.body;
  try {
    const usersRef = db.collection('users');
    const querySnapshot = await usersRef.where('username', '==', username).get();

    if (querySnapshot.empty) {
      return res.status(404).json({
        status: 404,
        message: "User not found",
        error: {
          details: "The user does not exist in the database.",
        },
      });
    }

    const userDoc = querySnapshot.docs[0];
    const userData = userDoc.data();
    let profilePictureUrl = userData.profilePicture;

    if (req.file) {
      const file = req.file;
      const id = userData.id;
      const bucketFileName = `profile_image-users/${id}-${file.originalname}`;
      const fileUpload = bucket.file(bucketFileName);

      const fileStream = fileUpload.createWriteStream({
        metadata: {
          contentType: file.mimetype,
        },
      });
      fileStream.end(file.buffer);

      await new Promise((resolve, reject) => {
        fileStream.on('finish', resolve);
        fileStream.on('error', reject);
      });

      profilePictureUrl = `https://storage.googleapis.com/${bucket.name}/${bucketFileName}`;

      if (userData.profilePicture) {
        const oldFileName = userData.profilePicture.split('/').pop();
        await bucket.file(`profile_image-users/${oldFileName}`).delete().catch(() => { });
      }
    }

    const updateData = {
      username: usernameUpdate || userData.username,
      age: ageUpdate ? parseInt(ageUpdate, 10) : userData.age,
      profilePicture: profilePictureUrl,
      updatedAt: new Date().toISOString(),
    };

    // Menggunakan set() dengan { merge: true } untuk memastikan pembaruan hanya untuk field yang diubah
    await userDoc.ref.set(updateData, { merge: true });

    return res.status(200).json({
      status: 200,
      message: "User updated successfully",
      data: {
        ...userData,
        ...updateData, // Menggabungkan data yang sudah diperbarui
      },
    });
  } catch (error) {
    return res.status(500).json({
      status: 500,
      message: "Failed to update user",
      error: {
        details: error.message,
      },
    });
  }
};



exports.deleteUserByUsername = async (req, res) => {
  const { username } = req.params;

  try {
    const usersRef = db.collection('users');
    const querySnapshot = await usersRef.where('username', '==', username).get();

    if (querySnapshot.empty) {
      return res.status(404).json({
        status: 404,
        message: "User not found",
        error: {
          details: "The user does not exist in the database.",
        },
      });
    }

    const userDoc = querySnapshot.docs[0];
    const userData = userDoc.data();

    if (userData.profilePicture) {
      const fileName = userData.profilePicture.split('/').pop();
      await bucket.file(`profile_image-users/${fileName}`).delete().catch(() => { });
    }

    await userDoc.ref.delete();

    return res.status(200).json({
      status: 200,
      message: "User deleted successfully",
    });
  } catch (error) {
    return res.status(500).json({
      status: 500,
      message: "Failed to delete user",
      error: {
        details: error.message,
      },
    });
  }
};