const express = require('express');
const router = express.Router();

const subjectCtrl = require('../controllers/subjectCtrl');

router.get('/get',subjectCtrl.getSubject);

module.exports = router;