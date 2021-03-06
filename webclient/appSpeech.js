var recorderApp = angular.module('recorder', [ ]);

recorderApp.controller('RecorderController', [ '$scope' , function($scope) {
	$scope.audio_context = null;
	$scope.stream = null;
	$scope.recording = false;
	$scope.encoder = null;
	$scope.ws = null;
	$scope.input = null;
	$scope.node = null;
	$scope.samplerate = 44100;
	$scope.autoSelectSamplerate = true;
	$scope.samplerates = [ 8000, 11025, 12000, 16000, 22050, 24000, 32000, 44100, 48000 ];
	$scope.compression = 0;
	$scope.compressions = [ 0/*, 1,2,3,4,5,6,7,8*/];
	$scope.recordButtonStyle = "red-btn";
    $scope.flacdata = {};
    $scope.flacdata.bps = 16;
    $scope.flacdata.channels = 1;
    $scope.flacdata.compression = 5;
    $scope.wav_format = false;
    $scope.outfilename_flac = "output.flac";
    $scope.outfilename_wav = "output.wav";
    
    $scope.result_mode = "file";//values: "asr" | "file" | TODO: "asr&file"
    $scope.asr_result = {
    		text: ""
    };
    
    $scope._google_api_key = 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx';
    $scope._asr_alternatives = 20;
    $scope.result_mode = 'asr';
    
    var __def_key = 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx';
    $scope.isNotASRCapable = $scope._google_api_key === __def_key;
    
	$scope.recordaswave = function(isUseWavFormat) {
        $scope.wav_format = isUseWavFormat;
    };
    
    $scope.setResultMode = function(isEnableASR){
    	
    	$scope.result_mode = isEnableASR? 'asr' : 'file';
    	
    };
      
	$scope.startRecording = function(e) {
		if ($scope.recording)
			return;
		
		console.log('start recording');//DEBUG
		
		$scope.encoder = new Worker('encoder.js');
        
        if ($scope.wav_format == true){
            $scope.encoder.postMessage({ cmd: 'save_as_wavfile'});
        }
        
		$scope.encoder.onmessage = function(e) {
			
			if (e.data.cmd == 'end') {

				var resultMode = $scope.result_mode;
				
				if(resultMode === 'file'){

	                var fname = $scope.wav_format ? $scope.outfilename_wav : $scope.outfilename_flac;
					$scope.forceDownload(e.data.buf, fname);
					
				}
				else if(resultMode === 'asr'){
					
					if($scope.wav_format){
						//can only use FLAC format (not WAVE)!
						alert('Can only use FLAC format for speech recognition!');
					}
					else {
						$scope.sendASRRequest(e.data.buf);
					}
					
				}
				else {
					
					console.error('Unknown mode for processing STOP RECORDING event: "'+resultMode+'"!');
				}
				
				
				$scope.encoder.terminate();
				$scope.encoder = null;
				
			} else if (e.data.cmd == 'debug') {
				
                console.log(e.data);
                
            } else {
            	
				console.error('Unknown event from encoder (WebWorker): "'+e.data.cmd+'"!');
            }
		};

		if(navigator.webkitGetUserMedia)
			navigator.webkitGetUserMedia({ video: false, audio: true }, $scope.gotUserMedia, $scope.userMediaFailed);
		else if(navigator.mozGetUserMedia)
			navigator.mozGetUserMedia({ video: false, audio: true }, $scope.gotUserMedia, $scope.userMediaFailed);
		else
			navigator.getUserMedia({ video: false, audio: true }, $scope.gotUserMedia, $scope.userMediaFailed);
				
	};

	$scope.userMediaFailed = function(code) {
		console.log('grabbing microphone failed: ' + code);
	};

	$scope.gotUserMedia = function(localMediaStream) {
		$scope.recording = true;
		$scope.recordButtonStyle = '';

		console.log('success grabbing microphone');
		$scope.stream = localMediaStream;
		
		var audio_context;
		if(typeof webkitAudioContext !== 'undefined'){
			audio_context = new webkitAudioContext;
		}else if(typeof AudioContext !== 'undefined'){
			audio_context = new AudioContext;
		}
		else {
			console.error('JavaScript execution environment (Browser) does not support AudioContext interface.');
			alert('Could not start recording audio:\n Web Audio is not supported by your browser!');
			
			return;
		}
        $scope.audio_context = audio_context;
		$scope.input = audio_context.createMediaStreamSource($scope.stream);
		
		if($scope.input.context.createJavaScriptNode)
			$scope.node = $scope.input.context.createJavaScriptNode(4096, 1, 1);
		else if($scope.input.context.createScriptProcessor)
			$scope.node = $scope.input.context.createScriptProcessor(4096, 1, 1);
		else
			console.error('Could not create audio node for JavaScript based Audio Processing.');

		
		var sampleRate = $scope.audio_context.sampleRate;
		console.log('audioContext.sampleRate: ' + sampleRate);//DEBUG
		if($scope.autoSelectSamplerate){
			$scope.samplerate = sampleRate;
		}
		
		console.log('initializing encoder with:');//DEBUG
        console.log(' bits-per-sample = ' + $scope.flacdata.bps);//DEBUG
        console.log(' channels        = ' + $scope.flacdata.channels);//DEBUG
        console.log(' sample rate     = ' + $scope.samplerate);//DEBUG
        console.log(' compression     = ' + $scope.compression);//DEBUG
		$scope.encoder.postMessage({ cmd: 'init', config: { samplerate: $scope.samplerate, bps: $scope.flacdata.bps, channels: $scope.flacdata.channels, compression:$scope.compression  } });

		$scope.node.onaudioprocess = function(e) {
			if (!$scope.recording)
				return;
            // see also: http://typedarray.org/from-microphone-to-wav-with-getusermedia-and-web-audio/
			var channelLeft  = e.inputBuffer.getChannelData(0);
			// var channelRight = e.inputBuffer.getChannelData(1);
			$scope.encoder.postMessage({ cmd: 'encode', buf: channelLeft});
		};

		$scope.input.connect($scope.node);
		$scope.node.connect(audio_context.destination);

		$scope.$apply();
	};

	$scope.stopRecording = function() {
		if (!$scope.recording) {
			return;
		}
		$scope.recordButtonStyle = "red-btn";
		console.log('stop recording');
		var tracks = $scope.stream.getAudioTracks()
		for(var i = tracks.length - 1; i >= 0; --i){
			tracks[i].stop();
		}
		$scope.recording = false;
		$scope.encoder.postMessage({ cmd: 'finish' });

		$scope.input.disconnect();
		$scope.node.disconnect();
		$scope.input = $scope.node = null;
	};
	
	//create A-element for data BLOB and trigger download
	$scope.forceDownload = function(blob, filename){
		debugger;

		var url = (window.URL || window.webkitURL).createObjectURL(blob);
		var link = window.document.createElement('a');
		link.href = url;
		link.download = filename || 'output.flac';
		//NOTE: FireFox requires a MouseEvent (in Chrome a simple Event would do the trick)
		var click = document.createEvent("MouseEvent");
		click.initMouseEvent("click", true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
		link.dispatchEvent(click);
	};
	
	$scope.num = 0;
	
	$scope.sendASRRequest = function(blob){

		var data = blob;
        var sample_rate = 	$scope.samplerate;

		var xhr = new XMLHttpRequest();
	  	xhr.open('POST', 'http://localhost:8080/bajtahack/speechcommand', true);
	  	xhr.onload = function(e) { console.log("loaded"); };
	  	xhr.setRequestHeader('Content-Type', 'application/octet-stream');
	  	xhr.onreadystatechange = function(){
	      	console.log("state: " + xhr.readyState);
	  	};
	  	// Listen to the upload progress.
	  	xhr.upload.onprogress = function(e) { console.log("uploading..."); };
	  	//xhr.setRequestHeader("Content-Type", "audio/x-flac; rate=" + sample_rate + ";");
	  	xhr.send(blob); 
	};

}]);

