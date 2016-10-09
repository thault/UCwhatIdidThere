var mongoose = require('mongoose');
var Schema =  mongoose.Schema;


//schema
var StampScheme = new Schema(
    {
        url: String,
        gps: String,
        description: String
    });


module.exports = mongoose.model('Stamp', StampScheme);