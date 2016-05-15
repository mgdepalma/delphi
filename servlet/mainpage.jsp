<!-- main page -->
<table border="0" align="center" width="95%">
<tr>
<td>&nbsp;</td>
<td><img src="images/<%= icon %>.png"></td>
<td align="center">
<h1 style="color:black; text-shadow:2px 2px 3px white; font-family:sans-serif">
Welcome <%= greet %>
</h1></td>
</tr>

<tr>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>
<div id="divdemo">
This is the home page for the delphi application used to illustrate the
source directory organization of a web application utilizing the principles
outlined in the Application Developer's Guide.

<p>To prove that they work, you can execute the following link:
<ul>
 <form name="servlet" method="post" action="oracle">
  <input type="hidden" name="request" value="weblog">
  A <a href="javascript:weblog()">weblog</a> from the backend data stores.
 </form>
 <script type="text/javascript">
  function weblog() {
    document.servlet.submit();
  }
 </script>
</ul>
</div>

<%@ include file="profiles.jsp" %>

</td></tr>
</table>
