const Quiz = require('../database/schema/quiz');

const createQuiz = (req,res)=>{
    const body = req.body;

    const quiz = new Quiz(body);
    quiz.save()
    .then((doc)=>{
        return res.status(200).json({
            success:true,
            quiz:doc,
        })
    }).catch((err)=>{
        console.log("Error",err);
        return res.status(500).json({
            success:false,
            msg:"Internal Server Error!"
        })
    })

}

const getQuiz = async (req,res)=>{
    
}

module.exports = {createQuiz,getQuiz};