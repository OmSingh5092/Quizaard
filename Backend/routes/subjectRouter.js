const express = require('express');
const router = express.Router();

const subjectCtrl = require('../controllers/subjectCtrl');

router.get('/get',subjectCtrl.getSubject);
router.post('/add',subjectCtrl.addSubject);

module.exports = router;