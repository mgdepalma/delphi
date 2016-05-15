/**
 * JavaScript functions used by HTML/JSP codes
 */
 const _NORMAL = "large";
 const _SELECT = "x-large";

 function services()
 {
  document.getElementById('Tmainpage').style.fontSize = _NORMAL;
  document.getElementById('Tlocation').style.fontSize = _NORMAL;
  document.getElementById('Tservices').style.fontSize = _SELECT;

  document.getElementById('mainpage').style.display = "none";
  document.getElementById('location').style.display = "none";
  document.getElementById('services').style.display = "block";
 }

 function mainpage(gripe)
 {
  document.getElementById('Tmainpage').style.fontSize = _SELECT;
  document.getElementById('Tlocation').style.fontSize = _NORMAL;
  document.getElementById('Tservices').style.fontSize = _NORMAL;

  document.getElementById('mainpage').style.display = "block";
  document.getElementById('location').style.display = "none";
  document.getElementById('services').style.display = "none";

  if (gripe) {
    document.getElementById('divdemo').style.display = "none";
    document.getElementById('divuser').style.display = "block";
  }
  else {
    document.getElementById('divdemo').style.display = "block";
    document.getElementById('divuser').style.display = "none";
  }
  document.getElementById('divpass').style.display = "none";
  document.getElementById('divacct').style.display = "none";
 }

 function locate()
 {
  document.getElementById('Tmainpage').style.fontSize =  _NORMAL;
  document.getElementById('Tlocation').style.fontSize =  _SELECT;
  document.getElementById('Tservices').style.fontSize =  _NORMAL;

  document.getElementById('mainpage').style.display = "none";
  document.getElementById('location').style.display = "block";
  document.getElementById('services').style.display = "none";
 }

 function CallPageSync(url, data)
 {
    var response;
    $.ajax({
        async: false,
        url: url,
        data: data,
        timeout: 4000,
        success: function(result) {
		response = result.replace(/(\r\n|\n|\r)/,"");
        },
        error: function(request, narrative, exception) {
		response = "["+ request.status +"] "+ request.statusText;
        }
    });
    return response;
 }

 function user()
 {
  var user = document.forms["user"]["username"].value;
  var pass = document.forms["user"]["password"].value;
  var hash = hex_sha1(pass);

  // use CallPageSync() to get the HMAC-SHA1 data from server
  var data = { request:'setting', name:'salt', redirect:'no' };
  var salt = CallPageSync("oracle", data);
  //document.getElementById('inside').innerHTML = hex_hmac_sha1(hash, salt);

  var post = document.getElementById('signin');
  document.forms["signin"]["username"].value = user;
  document.forms["signin"]["password"].value = hex_hmac_sha1(hash, salt);
  post.submit();
 }

 function passwd()
 {
  document.getElementById('divdemo').style.display = "none";
  document.getElementById('divuser').style.display = "none";
  document.getElementById('divpass').style.display = "block";
  document.getElementById('divacct').style.display = "none";
 }

 function recover()
 {
  var user = document.forms["pass"]["username"].value;
 }

 function register()
 {
  var wanted = document.forms["acct"]["username"].value;
  var passwd = document.forms["acct"]["password"].value;
  var verify = document.forms["acct"]["passverify"].value;

  if (passwd != verify)
   document.getElementById('inside').innerHTML = "<h3>Passwords Do Not Match</h3>";
  else {
   document.getElementById('inside').innerHTML = "<h3>Welcome "+ wanted +"</h3>";
  }
 }

 function signup()
 {
  document.getElementById('Tmainpage').style.fontSize = _SELECT;
  document.getElementById('Tlocation').style.fontSize = _NORMAL;
  document.getElementById('Tservices').style.fontSize = _NORMAL;

  document.getElementById('mainpage').style.display = "block";
  document.getElementById('location').style.display = "none";
  document.getElementById('services').style.display = "none";

  document.getElementById('divdemo').style.display = "none";
  document.getElementById('divuser').style.display = "none";
  document.getElementById('divpass').style.display = "none";
  document.getElementById('divacct').style.display = "block";
 }

 function signin()
 {
  document.getElementById('Tmainpage').style.fontSize = _SELECT;
  document.getElementById('Tlocation').style.fontSize = _NORMAL;
  document.getElementById('Tservices').style.fontSize = _NORMAL;

  document.getElementById('mainpage').style.display = "block";
  document.getElementById('location').style.display = "none";
  document.getElementById('services').style.display = "none";

  document.getElementById('divdemo').style.display = "none";
  document.getElementById('divuser').style.display = "block";
  document.getElementById('divpass').style.display = "none";
  document.getElementById('divacct').style.display = "none";
 }

 function profile()
 {
   document.getElementById('inside').innerHTML = "Profile";
 }

 function signout()
 {
  var post = document.getElementById('signout');
  post.submit();
 }
