//Global variables that allow index.js to use libraries for server.
const app = require('express');
const server = require('http').Server(app);
const io = require('socket.io')(server);
const uuid = require('node-uuid');
const Room = require('./room.js');
const _ = require('underscore')._;

/*
  Global variable/arrays that will store important user data
  that will be used through many socket events.
*/
//stores players by index
let players = [];
//stores players by their unique ID.
let person = [];
let rooms = [];

//variables that might be used.
let roomLimit = 0;

//listens for server connection (i.e 3000)
server.listen(3000, function(){
    console.log("Server is now running...");
});

/*
    Player connection triggers this event.
    When player connects these things should happen in order.
    1. index.js uses a check function to check if rooms array is empty. If rooms is empty, creates a new room and puts player in room.
    2. If rooms exist the array is checked from 0...i till a room with .private is set to false. Insert player into room, set room to private, game starts.
    3. If all rooms are set to private = true then a new room is created and player is put in room.
    4. When the status (private) of a room is set to true the game will officially start. Up to that point the game will remain paused.
    5. Once the game ends the points should be compared and a winner MUST be announced. After a 5 second countdown multiplayer.java will boot you to the menu screen.
 */
io.on('connection', function(socket){
    console.log("Player Connected!");
    console.log(rooms.length);
    socket.on("roomCheck", function(newPlayer) {
        if(rooms.length === 0){

        }else{
            for(let i = 0; i < rooms.length; i++){

            }
        }
    });

});