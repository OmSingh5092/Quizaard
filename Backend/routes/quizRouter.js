const express = require('express');
const router = express.Router();

const quizCtrl = require('../controllers/quizCtrl')
const verifyUser = require('../middlewares/verifyMW').user;

router.get('/get/faculty',verifyUser,quizCtrl.getQuizByFaculty);
router.get('/get/subject',verifyUser,quizCtrl.getQuizBySubject);
router.post('/create',verifyUser,quizCtrl.createQuiz);

module.exports = router;