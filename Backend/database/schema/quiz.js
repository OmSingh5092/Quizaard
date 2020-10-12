const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const student = new Schema(
    {  
        title:{
            type:String,
        },
        description:{
            type:String,
        },
        is_completed:{
            type:Boolean,
        },
        start_time:{
            type:String,
        },
        end_time:{
            type:String,
        },
        result:{
            type:String,
        },
        subject:{
            type:Array
        },
        faculties:{
            type:Array
        }
    },
    {
        collection:"quizzes"
    }
)

module.exports = mongoose.model("quiz",student);