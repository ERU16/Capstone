const fs = require('fs');
const path = require('path');

const dataFilePath = path.join(__dirname, '../assets/dataset/animals_data.json');

const readDataFromFile = () => {
  const data = JSON.parse(fs.readFileSync(dataFilePath, 'utf8'));
  return data.map((animal) => {
    return {
      ...animal,
      image_url: animal.image_url 
        ? `https://storage.googleapis.com/${process.env.GCP_STORAGE_BUCKET}/assets/${animal.image_url}`
        : null,
      sound_url: animal.sound_url 
        ? `https://storage.googleapis.com/${process.env.GCP_STORAGE_BUCKET}/assets/${animal.sound_url}`
        : null,
    };
  });
};

exports.getAllAnimals = (req, res) => {
  const animals = readDataFromFile();
  return res.status(200).json({
    status: 200,
    message: "Receive data successfully",
    data: animals,
  });
};

exports.getAnimalByName = (req, res) => {
  try {
    const animals = readDataFromFile();
    const validAnimals = animals.filter(
      (a) => a && typeof a.name === 'string' && a.name.trim() !== ''
    );

    const animal = validAnimals.find(
      (a) => a.name.toLowerCase() === req.params.animalName.toLowerCase()
    );

    if (!animal) {
      return res.status(404).json({
        status: 404,
        message: "Animal not found",
        error: {
          details: "The animal not found in database.",
        },
      });
    }
    return res.status(200).json({
      status: 200,
      message: "Receive data successfully",
      data: animal,
    });
  } catch (error) {
    return res.status(500).json({
      status: 500,
      message: "Internal server error",
      error: {
        details: error.message
      },
    });
  }
};

