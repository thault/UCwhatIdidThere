// BASE SETUP
// ======================================

// CALL THE PACKAGES --------------------
var express    = require('express');		// call express
var app        = express(); 				// define our app using express
var bodyParser = require('body-parser'); 	// get body-parser
var morgan     = require('morgan'); 		// used to see requests
var mongoose   = require('mongoose');
var User       = require('./models/user');
var Stamp      = require('./models/stamp');
var port       = process.env.PORT || 8080; // set the port for our app

// APP CONFIGURATION ---------------------
// use body parser so we can grab information from POST requests
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

// configure our app to handle CORS requests
app.use(function(req, res, next) {
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST');
    res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type, Authorization');
    next();
});

// log all requests to the console
app.use(morgan('dev'));

mongoose.Promise = global.Promise;
// connect to our database (hosted on modulus.io)
mongoose.connect('mongodb://localhost:27017/mydb');

// ROUTES FOR OUR API
// ======================================

// basic route for the home page
app.get('/', function(req, res) {
    res.send('Welcome to the home page!');
});

// get an instance of the express router
var apiRouter = express.Router();

// middleware to use for all requests
apiRouter.use(function(req, res, next) {
    // do logging
    console.log('Somebody just came to our app!');

    next(); // make sure we go to the next routes and don't stop here
});

// test route to make sure everything is working
// accessed at GET http://localhost:8080/api
apiRouter.get('/', function(req, res) {
    res.json({ message: 'hooray! welcome to our api!' });
});

// route to authenticate a user (POST http://localhost:8080/api/authenticate)
apiRouter.post('/authenticate', function(req, res) {

    // find the user
    User.findOne({
        email: req.body.email
    }).select('name email password').exec(function(err, user) {

        if (err) throw err;

        // no user with that username was found
        if (!user) {
            res.json({
                success: false,
                message: 'Authentication failed. User not found.'
            });
        } else if (user) {

            // check if password matches
            var validPassword = user.comparePassword(req.body.password);
            if (!validPassword) {
                res.json({
                    success: false,
                    message: 'Authentication failed. Wrong password.'
                });
            } else {
                // return the information including token as JSON
                res.json({
                    uuid: user._id,
                    stamps: user.stamps
                });
            }

        }

    });
});

// on routes that end in /users
// ----------------------------------------------------
apiRouter.route('/users')

// create a user (accessed at POST http://localhost:8080/users)
    .post(function(req, res) {

        var user = new User();		// create a new instance of the User model
        user.name = req.body.name;  // set the users name (comes from the request)
        user.email = req.body.email;  // set the users email (comes from the request)
        user.password = req.body.password;  // set the users password (comes from the request)

        user.save(function(err) {
            if (err) {
                // duplicate entry
                if (err.code != 11000)
                    return res.json({ success: false, message: 'A user with that email already exists. '});
                else
                    return res.send(err);
            }

            // return a message
            res.json({ message: 'User created!' });
        });

    })

    // get all the users (accessed at GET http://localhost:8080/api/users)
    .get(function(req, res) {
        User.find(function(err, users) {
            if (err) return res.send(err);

            // return the users
            res.json(users);
        });
    });

// on routes that end in /stamps
// ----------------------------------------------------
apiRouter.route('/stamps')

// create a stamp (accessed at POST http://localhost:8080/stamps)
    .post(function(req, res) {

        var stamp = new Stamp();		// create a new instance of the Stamp model
        stamp.name = req.body.name;  // set the stamp name (comes from the request)
        stamp.url = req.body.url;  // set the stamp url (comes from the request)
        stamp.gps = req.body.gps;  // set the stamp email (comes from the request)
        stamp.description = req.body.description;  // set the stamp password (comes from the request)

        stamp.save(function(err) {
            if (err) {
                // duplicate entry
                if (err.code != 11000)
                    return res.json({ success: false, message: 'That stamp already exists. '});
                else
                    return res.send(err);
            }

            // return a message
            res.json({ message: 'Stamp created!' });
        });

    })

    // get all the stamps (accessed at GET http://localhost:8080/api/stamps)
    .get(function(req, res) {
        Stamp.find(function(err, stamps) {
            if (err) return res.send(err);

            // return the stamps
            res.json(stamps);
        });
    });

// on routes that end in /users/:user_id
// ----------------------------------------------------
apiRouter.route('/stamps/:stamp_id')

// get the stamp with that id
    .get(function(req, res) {
        Stamp.findById(req.params.stamp_id, function(err, stamp) {
            if (err) return res.send(err);

            // return that stamp
            res.json(stamp);
        });
    })

    //may come back to get this working
    /*
    // update the user with this id
    .put(function(req, res) {
        User.findById(req.params.user_id, function(err, user) {

            if (err) return res.send(err);

            // set the new user information if it exists in the request
            if (req.body.name) user.name = req.body.name;
            if (req.body.email) user.email = req.body.email;
            if (req.body.password) user.password = req.body.password;

            // save the user
            user.save(function(err) {
                if (err) return res.send(err);

                // return a message
                res.json({ message: 'User updated!' });
            });

        });
    })
    */



    // delete the stamp with this id
    .delete(function(req, res) {
        Stamp.remove({
            _id: req.params.stamp_id
        }, function(err, stamp) {
            if (err) return res.send(err);

            res.json({ message: 'Successfully deleted' });
        });
    });



// on routes that end in /users/:user_id
// ----------------------------------------------------
apiRouter.route('/users/:user_id')

// get the user with that id
    .get(function(req, res) {
        User.findById(req.params.user_id, function(err, user) {
            if (err) return res.send(err);

            // return that user
            res.json(user);
        });
    })

    // update the user with this id
    .put(function(req, res) {
        User.findById(req.params.user_id, function(err, user) {

            if (err) return res.send(err);

            // set the new user information if it exists in the request
            if (req.body.name) user.name = req.body.name;
            if (req.body.email) user.email = req.body.email;
            if (req.body.password) user.password = req.body.password;

            // save the user
            user.save(function(err) {
                if (err) return res.send(err);

                // return a message
                res.json({ message: 'User updated!' });
            });

        });
    })

    .post(function(req, res) {

        // find the stamp
        Stamp.findOne({
            _id: req.body._id
        }).select('_id name url gps description').exec(function(err, stamp) {

         if (err) throw err;

            // no user with that username was found
            if (!stamp) {
                res.json({
                    success: false,
                    message: 'Authentication failed. Stamp not found.'
                });
            } else if (stamp) {

                User.findById(req.params.user_id, function(err, user){

                   // if(err) return res.send.(err);
                    console.log(user);
                    user.stamps.push(stamp._id);
                    user.save();
                    console.log(user.stamps);
                });
                // respond with data
                res.json({
                    name: stamp.name,
                    url: stamp.url,
                    gps: stamp.gps,
                    description: stamp.description

                });

            }

        });
    })

    // delete the user with this id
    .delete(function(req, res) {
        User.remove({
            _id: req.params.user_id
        }, function(err, user) {
            if (err) return res.send(err);

            res.json({ message: 'Successfully deleted' });
        });
    });

// REGISTER OUR ROUTES -------------------------------
app.use('/api', apiRouter);

// START THE SERVER
// =============================================================================
app.listen(port);
console.log('Magic happens on port ' + port);