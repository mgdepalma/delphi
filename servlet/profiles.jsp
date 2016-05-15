<!-- code for signing in, lost password, create account, and profile updates -->
<div id="divuser" style="display:none;" align="center">
 <form name="user" method="post" action="javascript:user()">
  <table background="images/padlock.png" width="480" height="250">
  <tr><td>
   <center><h2>Returning Users</a>
   </h2></center>
   &nbsp;&nbsp;&nbsp;
   <input type="text" size="50" name="username" placeholder="Enter your email"
							autofocus required>
   <br><br>
   &nbsp;&nbsp;&nbsp;
   <input type="password" size="50" name="password" placeholder="Password">
   <br>
   <center><font style="color:maroon;"><i>
   <a href="javascript:passwd()" style="color:maroon;">Forgot Password?</a>
   </i></font></center>
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   <button type="submit">Sign in</button>
  </td></tr>
  </table>
 </form>
</div>

<div id="divpass" style="display:none;" align="center">
 <form name="pass" method="post" action="javascript:weblog()">
  <table background="images/padlock.png" width="480" height="250">
  <tr><td>
   <center><h2>Password Recovery</h2></center>
   &nbsp;&nbsp;&nbsp;
   <input type="text" size="50" name="username" placeholder="Enter your email"
							autofocus required>
   <br><br>
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   <button type="submit">Recover</button>
  </td></tr>
  </table>
 </form>
</div>

<div id="divacct" style="display:none;" align="center">
 <form name="acct" method="post" action="javascript:register()">
  <table background="images/padlock.png" width="480" height="250">
  <tr><td>
   <center><h2>Create Account</h2></center>
   &nbsp;&nbsp;&nbsp;
   <input type="text" size="50" name="username" placeholder="Enter your email"
							autofocus required>
   <br><br>
   &nbsp;&nbsp;&nbsp;
   <input type="password" size="50" name="password" placeholder="Choose a password">
   <br>
   &nbsp;&nbsp;&nbsp;
   <input type="password" size="50" name="passverify" placeholder="Re-enter password">
   <br><br>
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   <button type="submit">Sign up</button>
  </td></tr>
  </table>
 </form>
</div>
