const express = require("express");
const router = express.Router();
const { getQuizByName, verifyAnswer } = require("../controllers/quizController");

router.get("/:animalName", getQuizByName);
router.post("/:animalName/verify", verifyAnswer);

module.exports = router;
