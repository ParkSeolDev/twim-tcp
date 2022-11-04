// import {net} from net;

var socket = null;

const request = new XMLHttpRequest();
const api = `http://localhost:8080/api/v1/order/all-orders`;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#messages").html("");
}

// function connect() {
//    var clientId = $("#clientId").val();
//    socket = io('http://127.0.0.1:9999?clientId='+clientId);
//     setConnected(true);
//     socket.on("data_event", function(message){
//         showMessage(message);
//     });
// }

function connect() {
    socket = io('http://127.0.0.1:9999');
     setConnected(true);
     socket.on("data_event", function(message){
         showMessage(message);
     });
 }


function disconnect() {
    socket.emit("disconnect_event", "User Disconnect Request");
    setConnected(false);
    console.log("Disconnected");
}

function sendData() {

    if(socket != null) {
        socket.emit("data_event", JSON.stringify({
            'data': $("#message").val()
        }));
    } else {
        console.log("Socket not initialized.");
        alert('Socket not connected');
    }

}

function receiveData() {
  
    if(socket != null){
      socket.on("data", getData()
    )
      
    } else {
      console.log("Socket not initialized.");
      alert('Socket not connected');
  }
}

function getData(){
  request.open("GET", api);
  request.onload = () => {
    showMessage(request.response)
  }
  request.send();
}

function showMessage(message) {
    $("#messages").append("<tr><td>" + message + "</td></tr>");
    console.log("Messages table updated!");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    
    $("#connect").unbind().click(function () {
        connect();
        
          setInterval(receiveData, 3000)
        
    });
    
    $("#disconnect").unbind().click(function () {
        disconnect();
    });
    $("#send").unbind().click(function () {
        sendData();
        
    });
});



// var net = require('net');

// net.connect({port: 9999, host: 'localhost'}, function(){
//     $("#connect").unbind().click(function () {
//         connect();
        
//           setInterval(receiveData, 3000)
        
//     });
// });