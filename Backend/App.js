const express = require('express');
const app = express();
const config = require('./config');
const database = require('./database/database');

require('./passport');

//Applying middlewares
app.use(express.json());

//Importing rotues
const signInRoute = require('./routes/signinRouter');
const studentRoute = require('./routes/studentRouter');
const facultyRoute = require('./routes/facultyRouter')

//Applying routes
app.use('/api/signin',signInRoute);
app.use('/api/student',studentRoute);
app.use('/api/faculty',facultyRoute);

app.listen(config.app.local.port, ()=>{
    console.log("\n\n App listening... \n\n");
})