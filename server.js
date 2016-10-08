
//initliaze packages
var express = require('express'),
    app = express,
    bodyParser = require('body-parser'),
    mongoose = require('mongoose'),
    port = process.env.PORT || 8080;


//body parse setup
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());
