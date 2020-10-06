const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const faculty = new Schema(
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
        collection:"faculties"
    }
)

module.exports = mongoose.model("faculties",faculty);