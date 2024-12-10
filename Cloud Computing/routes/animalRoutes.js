const express = require("express");
const router = express.Router();
const { getAllAnimals, getAnimalByName } = require("../controllers/animalController");

router.get("/", getAllAnimals);
router.get("/:animalName", getAnimalByName);

module.exports = router;
