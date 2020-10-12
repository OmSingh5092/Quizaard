const Student = require('../database/schema/student');

const updateProfile = (req,res)=>{
    const id = req.user.id;
    const body = req.body;
    console.log("Body",body);
    console.log("ID",id);
    Student.updateOne({_id:id},body)
    .then((doc)=>{
        return res.status(200).json({
            success:true,
            update:doc,
        })
    }).catch((err)=>{
        console.log("Error",err);
        return res.status(500).json({
            success:false,
            msg:"Update couldnot be done!",
        })
    })
}

const getProfile = (req,res)=>{
    const id = req.user.id;

    Student.findOne({_id:id})
    .then((doc)=>{
        return res.status(200),json({
            success:true,
            student:doc,
        })
    }).catch((err)=>{
        console.log("Error",err);
        return res.status(500).json({
            success:false,
            msg:"Internal Server Error!",
        })
    })
}

module.exports = {updateProfile,getProfile}