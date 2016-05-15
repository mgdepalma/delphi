<html>
<head>
<title>Delphi JSP Pages</title>
</head>
<body bgcolor=white>

<form id="31926767765874" method="post" action="oracle">
 <input type="hidden" name="request" value="prepare">
 <input type="hidden" name="redirect" value="see.jsp">
</form>
<script type="text/javascript">
function formAutoSubmit ()
{
 var frm = document.getElementById("31926767765874");
 frm.submit();
}
window.onload = formAutoSubmit;
</script>

<table border="0">
<tr>
<td align=center>
<img src="images/system.png">
</td>
<td>
<h1>delphi JSP Page</h1>
This is the output of a JSP page that is part of the delphi application.
</td>
</tr>
</table>
<br>
<%
 String salt = request.getParameter("seed");
 String username = request.getParameter("username");
 if (username == null) username = "to Delphi";
%>
<script>
 function collect()
 {
   var res = "<%= salt %>";
 }
</script>
<ul>
<h4>Welcome <%= username %></h4>
<%= salt %>
</ul>

</body>
</html>
