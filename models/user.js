var mongoose = require('mongoose');
var Schema =  mongoose.Schema;


//schema
var UserScheme = new Schema(
    {
        name: String,
        email: String,
        stamps: [StampScheme]
    });
