
//initliaze packages
var express = require('express'),
    app = express,
    bodyParser = require('body-parser'),
    mongoose = require('mongoose'),
    port = process.env.PORT || 8080;


//body parse setup
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());

//handling CORS requests
app.use(function(req, res, next) {
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST');
    res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type, Authorization');
    next();
});

//mongoose connection
mongoose.connect('mongodb://localhost:27017/test');

//routing
