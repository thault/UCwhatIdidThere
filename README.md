# UCwhatIdidThere


##API Calls:
### 1. /authenticate
* post: email password
 * returns fail or user uuid/stamp array( String array of stamp uuid)

###2. /user
* post: name email password
 * returns fail or success message
* get:  resturns all users in the db
    
###3. /stamps
* post:  name url gps description
 * returns fail or success message
          
###4. /stamps/stamp_id
* get: returns the stamp
* delete: its in the word

###5. /users/user_id
* get: returns the user
* put: updates information provided
  * return success message
* post: _id (really should have changed that but oh well, that is the **stamp** _id)
  * adds the stamp to the users stamp array
  * returns stamp: name, url, gps, description
* delete: again, it's in the name
