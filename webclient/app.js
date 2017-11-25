"use strict";

function App(){}

App.prototype = {

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