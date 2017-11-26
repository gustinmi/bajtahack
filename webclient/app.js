"use strict";

function App(){}

App.prototype = {

    renderDeviceState: function izpisi(jsonDef, jsonState) { // izpis enega service / naprave
         var output;
         //debugger;
         if (jsonDef.type === "TEMPERATURE") {
             output = '<div><button data-device="' + jsonDef.device + '" data-service="' + jsonDef.service + '">TEMP</button><span></span></div>';
         } else if (jsonDef.type === "HUMIDTY") {
             output = '<div><button data-device="' + jsonDef.device + '" data-service="' + jsonDef.service + '">HUMIDITY</button><span></span></div>';
         } else if (jsonDef.type === "BUTTON") {
             output = '<div><button data-device="' + jsonDef.device + '" data-service="' + jsonDef.service + '">BUTTON</button><span></span></div>';
         } else if (jsonDef.type === "MOTION") {
             output = '<div class="stateData">';
             output += '<input type="hidden" value="' + jsonDef.service + '"">';
             output += '<span>GIBANJE</span>';
             if (jsonState){
                if (jsonState.value === "0") {
                     output += '<span>NE</span>';
                }
                else {
                     output += '<span>DA</span>';
                }
             }
             output += "</div>";
         } else if (jsonDef.type === "WATER") {
             output = '<div class="stateData">';
             output += '<input type="hidden" value="' + jsonDef.service + '"">';
             output += '<span>VODA</span>';

             if (jsonState){

                 if (jsonState.value === "0") {
                     output += '<span>DA</span>';
                 }
                 else {
                     output += '<span>NE</span>';
                 }
             }

             output += "</div>";
         } else if (jsonDef.type === "LIGHT") {
             output = '<div class="stateData">';
             output += '<input type="hidden" value="' + jsonDef.service + '"">';
             if (jsonState){
                 if (jsonState.value === "0") {
                     output += '<span>NE GORI</span>';
                 }
                 else {
                     output += '<span>GORI</span>';
                 }
             }
             output += '<button>LIGHT</button>';
             output += "</div>";
         } else {
             output = "<div>&nbsp;</div>";
         }
         return output;
    },




    renderState: function(deviceId, jsonDef, jsonState){ // gre cez vse service
        // debugger;
        var that = this,
            jqFloor = $(".main .floor." + deviceId),
            buffAll = [],
            stateLookupTable = {};

        $.each(jsonState, function(idx1, eltState){

            stateLookupTable[eltState.service] = eltState;

        });

        //debugger;
        $.each(jsonDef, function(idx, elt){

            if (stateLookupTable[elt.service]){
                buffAll.push(that.renderDeviceState(elt, stateLookupTable[elt.service]));    
            }else{
                buffAll.push(that.renderDeviceState(elt));
            }

        });

        $('.room1', jqFloor).html(buffAll.join(""));

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