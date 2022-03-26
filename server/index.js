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
let players = [];
let person = [];
let rooms = [];

//variables that might be used.
let roomLimit = 0;

server.listen(3000, function(){
    console.log("Server is now running...");
});

io.on('connection', function(socket){








});