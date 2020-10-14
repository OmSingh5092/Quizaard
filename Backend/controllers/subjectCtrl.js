const Subject = require('../database/schema/subject');

const getSubject = async (req,res)=>{
    const id = req.headers.id;

    try{
        const subject = await Subject.findById(id);

        return res.status(200).json({
            success:true,
            subject:subject,
        })
    }catch(err){
        return res.status(500).json({
            success:false,
            msg:"Internal Server Error",
        })
    }
}

const addSubject = (req,res)=>{

    const body = req.body;

    const subject = new Subject(body);

    subject.save()
    .then((doc)=>{
        return res.status(200).json({
            success:true,
            subject:doc,
        })
    }).catch((err)=>{
        return res.status(500).json({
            success:false,
            msg:"Internal Server error!"
        })
    })
}

module.exports = {getSubject,addSubject};