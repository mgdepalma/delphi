<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="description" content="GOULD middleware">
<meta name="keywords" content="GOULD,JSP,JavaScript,middleware,multi-tier,postgres">
<meta name="author" content="Mauro DePalma">

<title>Delphi Home Page</title>

<%
 HttpSession seance = request.getSession(false);
 String ipv4 = (String)seance.getAttribute("ipv4");

 String error = (String)request.getAttribute("error");
 String landing = (String)request.getAttribute("landing");

 String icon  = (String)request.getAttribute("avatar");
 String greet = (String)request.getAttribute("name");
 String name  = (String)request.getAttribute("name");
 String uuid  = (String)request.getAttribute("uuid");

 if(error == null) error = "";
 if(greet == null) greet = "to Delphi";
 if(landing == null) landing = "mainpage";
 if(icon == null) icon = "system";
 if(name == null) name = "";
%>
<link href="script/style.css" rel="stylesheet" type="text/css">
<style type="text/css">
 body {
    -o-background-size: cover;
    -moz-background-size: cover;
    -webkit-background-size: cover;
    background: url(images/backdrop.jpg) no-repeat center center fixed;
    background-size: cover;
 }
</style>

<script src="script/jquery-1.11.3.js"></script>
<script src="script/functions.js"></script>
<script src="script/sha1.js"></script>
<script>
 function seeding()
 {
  var gripe = "<%= error %>";
  var place = "<%= landing %>";
  var userid = "<%= uuid %>";

  if (userid != "null") {
    document.getElementById('enter').style.display = "none";
    document.getElementById('leave').style.display = "block";
  }
  else {
    document.getElementById('enter').style.display = "block";
    document.getElementById('leave').style.display = "none";
  }

  if (place == "services")
    services(gripe);
  else
    mainpage(gripe);
 }
</script>
</head>
<body onload="seeding()">

<!-- forms -->
<form id="signin" method="post" action="oracle">
 <input type="hidden" name="request" value="signin">
 <input type="hidden" name="username">
 <input type="hidden" name="password">
</form>

<form id="signout" method="post" action="oracle">
 <input type="hidden" name="request" value="signout">
 <input type="hidden" name="username">
</form>


<table cellspacing="0" width="90%">
<tr>
 <td>&nbsp;&nbsp;</td>
 <!-- product and services, left side -->
 <td>
 <div id="nav" style="text-align: left">
 <ul>
  <li id="Tmainpage">
   <a href="javascript:mainpage()"><img src="images/home.png"></a>
  </li>
  <li id="Tlocation">
   <a href="javascript:locate()">Geolocation</a>
  </li>
  <li id="Tservices">
   <a href="javascript:services()">Services</a>
  </li>
 </ul>
 </td>

 <td>&nbsp;</td>
 <!-- user status, right side -->
 <td align="right">
 <div id="nav" style="text-align: right">
 <ul>
  <li id="enter">
   <a href="javascript:signin()">Sign in</a>
   <b>/</b>
   <a href="javascript:signup()">Create Account</a>
  </li>
  <li id="leave">
   <%= name %>
   &diams;<a href="javascript:profile()">Profile</a>&diams;
   <a href="javascript:signout()">Sign out</a>
  </li>
 </ul>
 </div>
 </td>
</tr>
</table>

<!-- render results inside div -->
<div id="inside" style="height:50px; width:70%;"></div>

<!-- include code that make up site pages -->
<div id="mainpage" style="display:none;">
<%@ include file="mainpage.jsp" %>
</div>
<div id="location" style="display:none;">
<%@ include file="location.jsp" %>
</div>
<div id="services" style="display:none;">
<%@ include file="services.jsp" %>
</div>

<script>
 //document.getElementById('inside').innerHTML = "<%= ipv4 %>";
</script>
<h3 style="color:maroon"><%= error %></h3>

</body>
</html>

