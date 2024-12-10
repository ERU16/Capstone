const fs = require('fs');
const path = require('path');

const dataFilePath = path.join(__dirname, '../assets/dataset/animals_quiz.json');

const readDataFromFile = () => {
    try {
        const data = fs.readFileSync(dataFilePath, 'utf8');
        return JSON.parse(data);
    } catch (error) {
        return res.status(500).json({
            status: 500,
            message: "Error reading data from file",
            error: {
                data: [],
                details: error.message,
            },
        });
    }
};

exports.getQuizByName = (req, res) => {
    try {
        const animals = readDataFromFile();
        const validAnimals = animals.filter(
            (a) => a && typeof a.animal_name === 'string' && a.animal_name.trim() !== ''
        );
    
        const animalQuiz = validAnimals.find(
            (a) => a.animal_name.toLowerCase() === req.params.animalName.toLowerCase()
        );
    
        if (!animalQuiz) {
            return res.status(404).json({
                status: 404,
                message: "Quiz not found",
                error: {
                    details: "The quiz animal not found in database.",
                },
            });
        }
        return res.status(200).json({
            status: 200,
            message: "Quiz animal data successfully",
            data: animalQuiz,
          });
    } catch (error) {
        return res.status(500).json({
            status: 404,
            message: "Internal server error",
            error: {
              details: error.message
            },
          });
    }
};


exports.verifyAnswer = (req, res) => {
    try {
        const animals = readDataFromFile();
        const validAnimals = animals.filter(
            (a) => a && typeof a.animal_name === 'string' && a.animal_name.trim() !== ''
        );

        const animalQuiz = validAnimals.find(
            (a) => a.animal_name.toLowerCase() === req.params.animalName.toLowerCase()
        );

        if (!animalQuiz) {
            return res.status(404).json({
                status: 404,
                message: "Quiz not found",
                error: {
                    details: "The quiz animal not found in database.",
                },
            });
        }

        const userAnswer = req.body.answer;

        if (!userAnswer) {
            return res.status(400).json({
                status: 400,
                message: "Answer not provided",
            });
        }

        if (userAnswer.toLowerCase() === animalQuiz.correct_answer.toLowerCase()) {
            return res.status(200).json({
                status: 200,
                message: "Correct answer!",
                data: {
                    correct: true,
                    fun_fact: animalQuiz.fun_fact,
                },
            });
        } else {
            return res.status(200).json({
                status: 200,
                message: "Incorrect answer",
                data: {
                    correct: false,
                    correct_answer: animalQuiz.correct_answer,
                },
            });
        }
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