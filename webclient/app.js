"use strict";

function App(){}

App.prototype = {

    renderDeviceState : function(dState){

        var gpio = state.service;
        var type = state.type; 
        var value = state.value;

        var jqState = $('<div><span class="type"></span></div>');

        { "service":"23", "type":"gib", "value":"0"}
    },

    renderState: function(deviceId, state){
        debugger;
        var jqFloor = $(".main .floor." + deviceId);
        $('.room1', jqFloor).text(state);

    },


    getState : function(url, cb){

        $.ajax({
            "dataType": "json",
            "type": 'POST',
            "url": window.serverUrl + url,
            "success": function(resp) {
                 console.log(resp);   
                 cb && cb(resp);
            }
        });

    }


};

var app = new App();
window.app = app;