"use strict";

function App(){}

App.prototype = {

    renderDeviceState: function izpisi(json) {
        var output = "<div>&nbsp;</div>"; 
        if (json.type === "gib") {
            output = '<div class="stateData">';
            output += '<input type="hidden" value="' + json.service + '"">';

            output += '<span>GIBANJE</span>';
            if (json.value === "0") {
                output += '<span>NE</span>';
            }
            if (json.value === "1") {
                output += '<span>DA</span>';
            }
            output += "</div>";
        }
        if (json.type === "voda") {
            output = '<div class="stateData">';
            output += '<input type="hidden" value="' + json.service + '"">';
            output += '<span>VODA</span>';
            if (json.value === "0") {
                output += '<span>NE</span>';
            }
            if (json.value === "1") {
                output += '<span>DA</span>';
            }
            output += "</div>";
        }
        if (json.type === "luc") {
            output = '<div class="stateData">';
            output += '<input type="hidden" value="' + json.service + '"">';
            if (json.value === "0") {
                output += '<span>NE GORI</span>';

            }
            if (json.value === "1") {
                output += '<span>GORI</span>';

            }
            output += '<button>Toggle</button>';
            output += "</div>";
        }
        if (json.type === "temperature") {
            output = '<div class="stateData">';
            output += '<input type="hidden" value="' + json.service + '"">';
            output += '<span>TEMPERATURA</span>';
            output += '<button>Posodobi temperaturo</button>';
            output += "</div>";
        }
        return output;
    },


    renderState: function(deviceId, state){
        // debugger;
        var that = this,
            jqFloor = $(".main .floor." + deviceId),
            buffAll = [];

        $.each(state, function(idx, elt){
            buffAll.push(that.renderDeviceState(elt));
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