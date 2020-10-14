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

module.exports = {getSubject};