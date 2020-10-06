const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const student = new Schema(
    {
        name:{
            type:String
        },
        email:{
            type:String
        },
        phone:{
            type:Number
        },
    },{
        timestamps:true
    },
    {
        collection:"students"
    }
)

module.exports = mongoose.model("students",student);