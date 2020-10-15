const Quiz = require('../database/schema/quiz');
const Student = require('../database/schema/student');

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

const getQuizByFaculty = (req,res)=>{
    const id = req.user.id;

    Quiz.find({faculty:id})
    .then((docs)=>{
        return res.status(200).json({
            success:true,
            quizzes: docs,
        })
    }).catch((err)=>{
        return res.status(500).json({
            success:false,
            msg:"Internal Server Error!",
        })
    })
}

const getQuizBySubject = (req,res)=>{
    const id = req.headers.id;

    Quiz.find({subject:id})
    .then((docs)=>{
        return res.status(200).json({
            success:true,
            quizzes:docs,
        })
    }).catch((err)=>{
        return res.status(500).json({
            success:false,
            msg:"Internal Server Error!",
        })
    })
}

const getQuizByStudent = async (req,res)=>{
    const id = req.user.id;

    try{
        const student = await Student.findById(id);
    
        const condition = [];
        student.subjects.map((item,index)=>{
            condition.push({subject:item});
        })

        const quizzes = await Quiz.find({$where:condition});

        return res.status(200).json({
            success:true,
            quizzes:quizzes,
        })
    }catch(err){
        console.log("Error",err);
        res.status(500).json({
            success:false,
            msg:"Internal Server Error!",
        })
    }
    

}

module.exports = {createQuiz,getQuizByFaculty,getQuizBySubject,getQuizByStudent};