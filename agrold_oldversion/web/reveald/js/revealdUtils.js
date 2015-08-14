var storage = (function() {
    var uid = new Date,
        storage,
        result;
    try {
      (storage = window.localStorage).setItem(uid, uid);
      result = storage.getItem(uid) == uid;
      storage.removeItem(uid);
      return result && storage;
    } catch(e) {}
  }());

String.prototype.width = function(font) {
	  var f = font || '13px sans-serif',
	      o = $('<div>' + this + '</div>')
	            .css({'position': 'absolute', 'float': 'left', 'white-space': 'nowrap', 'visibility': 'hidden', 'font': f})
	            .appendTo($('body')),
	      w = o.width();

	  o.remove();

	  return w;
}

var unique = function(origArr) {  
    var newArr = [],  
        origLen = origArr.length,  
        found,  
        x, y;   
    for ( x = 0; x < origLen; x++ ) {  
        found = undefined;  
        for ( y = 0; y < newArr.length; y++ ) {  
            if ( origArr[x].key === newArr[y].key ) {   
              found = true;  
              break;  
            }  
        }  
        if ( !found) newArr.push( origArr[x] );      
    }  
   return newArr;  
};  

function writeCookie(name,value,days) {
    var date, expires;
    if (days) {
        date = new Date();
        date.setTime(date.getTime()+(days*24*60*60*1000));
        expires = "; expires=" + date.toGMTString();
            }else{
        expires = "";
    }
    document.cookie = name + "=" + value + expires + "; path=/";
}

function readCookie(name) {
    var i, c, ca, nameEQ = name + "=";
    ca = document.cookie.split(';');
    for(i=0;i < ca.length;i++) {
        c = ca[i];
        while (c.charAt(0)==' ') {
            c = c.substring(1,c.length);
        }
        if (c.indexOf(nameEQ) == 0) {
            return c.substring(nameEQ.length,c.length);
        }
    }
    return '';
}

function makeid()
{
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    for( var i=0; i < 5; i++ )
        text += possible.charAt(Math.floor(Math.random() * possible.length));
    return text;
}

function createDistinctURI(name){
	return 'http://chem.deri.ie/granatum/' + name.replace(/\s/g, '');
}

function createDistinctURL(name){
	return 'http://chem.deri.ie/granatum/' + name.replace(/\s/g, '');
}

function parseURI(uri){
	var url = decodeURIComponent(uri).replace("?",""); 
	var parameters = {};
	var pairs = url.split('&');
	$.each(pairs, function(i, v){
		var pair = v.split('=');	    
		parameters[pair[0]] = pair[1];
	});
	return parameters;
}

var allQueryParams = parseURI(window.location.search);

function createURI(queryParameters){	
	var uri = "";
	if(queryParameters.task != null)
		uri += "?task="+queryParameters.task;
	if(queryParameters.reveal != null)
		uri += "&reveal="+queryParameters.reveal;
	if(queryParameters.nodes != null) {
		uri += (uri == "" ? "?" : "&") + "nodes="+queryParameters.nodes;
		if(queryParameters.links != null) 
			uri += "&links="+queryParameters.links;
		if(queryParameters.filters != null)
			uri += "&filters="+queryParameters.filters;
	}
	if(queryParameters.flexible != null)
		uri += (uri == "" ? "?" : "&") + "flexible=" + queryParameters.flexible;
	console.log(allQueryParams.folderId);
	if(allQueryParams.folderId != null)
		uri += (uri == "" ? "?" : "&") + "folderId=" + allQueryParams.folderId;
	return uri;
}

/*
function DisplayIP(response) {
	if (storage) {
		storage.setItem('ip', response.ip);
	} else {
		writeCookie('ip', response.ip, 1);
	}
}*/