///
// delphi::oracle servlet
//
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.nio.charset.Charset;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.sql.DataSource;

import delphi.*;

@WebServlet(name = "oracle", urlPatterns = {"/oracle"})
public class oracle extends HttpServlet
{
    private static final String _self = "oracle";

    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");

    private static final char _session_active  = 'A';
    private static final char _session_invalid = 'I';
    private static final char _session_logout  = 'L';
    private static final char _session_timeout = 'T';

    private static final int _nologin = 65534;
    private static final int _refused = 1;

    private static String _driver;	// JDBC driver {oracle,postgres}
    private static String _redirect;	// redirect URL, may be optional
    private static String _timestamp;	// current date/time stamp
    private static String _salt;	// HMAC-SHA1 data

    private static int _guestid = 500;	// should we get from someplace?
    private static int _timeout = 0;	// session timeout seconds

    private String DBGpass;
    private String DBGhash;
    private String DBGdigest; 

    // establish connection with database backend
    protected Connection getConnection(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out)
    {
	InitialContext ctx = null;

        try {
           ctx = new InitialContext();
        } catch (NamingException ex) {
	   printStackTrace(out, "NamingException for InitialContext", ex);
           return null;
        }
	Properties prop = new Properties();

	try {
	   String path = getServletContext().getRealPath("/WEB-INF");
	   prop.load(new FileInputStream(path + "/properties"));
	} catch (IOException ex) {
	   printStackTrace(out, "IOexception", ex);
           return null;
	}
        DataSource ds = null;

        try {
           _driver = prop.getProperty("dbms");
	   ds = (DataSource)ctx.lookup("java:comp/env/jdbc/" + _driver);
	   _timestamp = _driver.equals("oracle")
				? "current_timestamp" : "clock_timestamp()";
        } catch (NamingException ex) {
	   printStackTrace(out, "NamingException", ex);
           return null;
        }
	Connection connection = null;

        try {
	   connection = ds.getConnection();

	   String param = prop.getProperty("timeout");
	   if (param != null) _timeout = Integer.parseInt(param);

	   _redirect = prop.getProperty("redirect");
	   _salt = getSetting(connection, "salt");	// default HMAC data value
        } catch (SQLException ex) {
	   printStackTrace(out, "SQLException", ex);
	}
	return connection;
    }

    // fetch session ID attribute
    protected String getAttribute(Connection connection, String sessid, String name)
        throws SQLException
    {
	Statement st = connection.createStatement();
	ResultSet rs = st.executeQuery("SELECT userid FROM sessions" +
					" WHERE sessid = '"+ sessid +"'");
	String value = null;

	if (rs != null && rs.next()) {
	  String uid = rs.getString(1);

	  if (name.equals("userid"))
	    value = uid;
	  else {
	    rs = st.executeQuery("SELECT "+ name +" FROM users WHERE userid = "+ uid);
	    if (rs != null && rs.next()) value = rs.getString(1);
	  }
	}

	rs.close();
	st.close();

	return value;
    }

    // fetch avatar (identifying part of an image) for a user
    protected String getAvatar(Connection connection, String user)
        throws SQLException
    {
	Statement st = connection.createStatement();
	ResultSet rs = st.executeQuery(
	     "SELECT avatar FROM users WHERE username = '"+ user +"'");

	String icon = (rs != null && rs.next()) ? rs.getString(1) : null;

	rs.close();
	st.close();

	return icon;
    }

    // fetch display name given a user ID
    protected String getName(Connection connection, int uid)
        throws SQLException
    {
	Statement st = connection.createStatement();
	ResultSet rs = st.executeQuery("SELECT last,first FROM users WHERE userid = "+ uid);

	String name = (rs != null && rs.next()) ? rs.getString(2)+" "+rs.getString(1) : null;

	rs.close();
	st.close();

	return name;
    }

    // fetch value of a setting stored in the database backend
    protected String getSetting(Connection connection, String name)
	throws SQLException
    {
	Statement st = connection.createStatement();
	ResultSet rs = st.executeQuery("SELECT value FROM settings" +
					" WHERE name = '"+ name +"'");
	String value = null;
	if (rs != null && rs.next()) value = rs.getString(1);

	rs.close();
	st.close();

	return value;
    }

    // update data of existing session or insert data for one just created 
    protected String sessionEnter(Connection connection, String sessid, Long inet)
        throws SQLException
    {
	Statement st = connection.createStatement();
	String sql = "SELECT sessid,userid FROM sessions WHERE inet = "+ inet;
	ResultSet rs = st.executeQuery(sql);

	if (rs != null && rs.next())
	  // possible that a new session was created
	  sql = "UPDATE sessions SET sessid ='"+ sessid +"'" +
		",accessed = "+ _timestamp + " WHERE inet = "+ inet;
	else {
	  String status = String.valueOf(_session_active);
	  sql = "INSERT INTO sessions VALUES ('"+ sessid +"',"+ inet +"," +
		_guestid +","+ _timestamp +","+ _timestamp +",'"+ status +"')";
	}
	st.execute(sql);

	rs.close();
	st.close();

	// very simple for now, return session Id
	return sessid;
    }

    // delete session data and invalidate 
    protected void sessionLeave(Connection connection, HttpSession session)
        throws SQLException
    {
	Statement st = connection.createStatement();

	st.execute("DELETE from sessions" +
		   " WHERE sessid = '"+ session.getId() +"'");
	st.close();

	session.invalidate();
    }

    // incoming HttpServletRequest central processing`
    protected void processRequest(HttpServletRequest request,
					HttpServletResponse response)
	throws ServletException, IOException
    {
        PrintWriter out = response.getWriter();

	String command = request.getParameter("request");
	if (command == null) {
	  response.sendError(406);	// Not Acceptable
	  return;
	}

        try {
           Connection connection = getConnection(request, response, out);
	   if (connection == null) {
	     //response.sendError(403);	// Forbidden
	     response.sendError(402);	// Payment Required
	     return;
	   }

	   // initialize parameters
	   HttpSession session = request.getSession(true);
	   String requestor = request.getRemoteAddr();
	   Long inet = IPv4toLong(requestor);

	   // session global attributes
	   session.setAttribute("ipv4", requestor);

	   // authentication token
	   String sessid = sessionEnter(connection, session.getId(), inet);
	    
	   if (command.equals("weblog")) {
	     response.setContentType("text/html;charset=UTF-8");

	     HTMLheader(out, "Servlet " + _self);
	     out.println("<h1>"+ request.getContextPath() +"/"+_self+"</h1>");
	     doBlog(connection, out, session, requestor);
	     HTMLfooter(out);
	   }
	   else {
	     response.setContentType("text/plain;charset=UTF-8");

	     // potentially common across requests
	     String url = request.getParameter("redirect");
	     if (url == null) url = _redirect;

	     if (command.equals("server")) {
	       try {
		 out.println( getServerName() );
	       }
	       catch (UnknownHostException ex) {
	       }
	     }
	     else if (command.equals("setting")) {
	       String name = request.getParameter("name");
	       if (name != null) {
		 String value = getSetting(connection, name);

		 if (url.equals("no"))
		   out.println(value);
		 else {
		   request.setAttribute(name, value);
	           redirect(request, response, url);
		 }
	       }
	     }
	     else if (command.equals("signin")) {
	       String user = request.getParameter("username");
	       String pass = request.getParameter("password");

	       if (user != null && pass != null) {
		 int uid = signin(connection, sessid, inet, user, pass);

		 if (uid == _nologin)
		   request.setAttribute("error", user +" not found");
		 else if (uid == _refused)
		   request.setAttribute("error", user +" invalid passwd");
		   //request.setAttribute("error", DBGpass+" | "+DBGhash+" | "+DBGdigest);
		 else {
		   request.setAttribute("avatar", getAvatar(connection, user));
		   request.setAttribute("name", getName(connection, uid));
		   request.setAttribute("uuid", String.valueOf(uid));
		 }
		 request.setAttribute("salt", _salt);
		 redirect(request, response, url);
	       }
	       else {
	         response.sendError(400);	// Syntactically Incorrect
	       }
	     }
	     else if (command.equals("signout")) {
	       sessionLeave(connection, session);
	       redirect(request, response, url);
	     }
	     else {
	       response.sendError(404);		//  Not Found
	     }
	   }
           connection.close();
        } catch (SQLException ex) {
            printStackTrace(out, "SQLexception", ex);
        } catch (Exception ex) {
            printStackTrace(out, "Exception", ex);
        } finally {
            out.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
				HttpServletResponse response)
	throws ServletException, IOException
    {
	String page = request.getParameter("page");

	if (page != null && page.equals("main")) {
	  request.setAttribute("request", "setting");
	  request.setAttribute("name", "landing");
	  redirect(request, response, _self);
	}
	else {
	  processRequest(request, response); // only for testing
	  //response.sendError(405);	// Method Not Allowed
	}
    }

    @Override
    protected void doPost(HttpServletRequest request,
				HttpServletResponse response)
	throws ServletException, IOException
    {
        processRequest(request, response);
    }

    // print weblog from database backend
    protected void doBlog(Connection connection, PrintWriter out,
				HttpSession session, String requestor)
            throws SQLException
    {
	Date created  = new Date(session.getCreationTime());
        Date accessed = new Date(session.getLastAccessedTime());
	Long inet = IPv4toLong(requestor);
	String sessid = session.getId();

        out.println("Session => "+ sessid +"<br>");
        out.println("Accessed => "+ accessed +"<br>");
        out.println("Created => "+ created +"<br>");
        out.println("Requestor => "+ requestor +" ("+ inet +")");

	Statement st = connection.createStatement();
	ResultSet rs = st.executeQuery("SELECT id,created,story FROM blog");

	while (rs.next()) {
	  out.println("<h3>" + rs.getString(1) + " "
			     + rs.getString(2) + " "
			     + rs.getString(3) + "</h3>");
	}
	rs.close();
	st.close();
    }

    // authenticate user based on IPv4, username and password
    protected int signin(Connection connection, String sessid, Long inet,
				String user, String pass)
	throws SQLException
    {
	Statement st = connection.createStatement();
	String sql = "SELECT password,userid FROM users" +
		     " WHERE username = '"+ user +"'";

	ResultSet rs = st.executeQuery(sql);
	String status = String.valueOf(_session_active);
	int uid = _nologin;

	if (rs != null && rs.next()) {
	  String hash = rs.getString(1); // password hash
	  String digest = SHA1.hex_hmac_sha1(hash, _salt);

	  /* use UTF-8 salt for enhanced security */
	  //String digest = SHA1.hex_hmac_sha1(hash, encodeUTF8(_salt));
	  uid = _refused;

	  DBGpass = pass;
	  DBGhash = hash;
	  DBGdigest = digest;

	  if (digest.equals(pass)) {
	    uid = Integer.parseInt(rs.getString(2));
	    sql = "SELECT inet FROM sessions WHERE sessid = '"+ sessid +"'";
	    rs = st.executeQuery(sql);

	    if (rs != null && rs.next())
	      // TBI check if session has exceeded timeout when applicable
	      sql = "UPDATE sessions SET userid = " + uid +
			",accessed = "+ _timestamp +
			" WHERE sessid = '"+ sessid +"'";
	    else
              sql = "INSERT INTO sessions VALUES ('"+ sessid +"',"+ inet +"," +
		  uid +","+ _timestamp +","+ _timestamp +",'"+ status +"')";

	    st.execute(sql);
	  }
	}

	rs.close();
	st.close();

	return uid;
    }

    // print out HTML header
    protected void HTMLheader(PrintWriter out, String title)
    {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>"+ title +"</title>");
        out.println("</head>");
        out.println("<body>");
    }

    // print out HTML footer
    protected void HTMLfooter(PrintWriter out)
    {
        out.println("</body>");
        out.println("</html>");
    }

    // return IPv4 address as long integer
    protected static long IPv4toLong(String ip)
    {
        String[] octect = ip.split("\\.");
        return (Long.parseLong(octect[0]) << 24) +
               (Integer.parseInt(octect[1]) << 16) +
               (Integer.parseInt(octect[2]) << 8) +
               Integer.parseInt(octect[3]);
    }

    protected void redirect(HttpServletRequest request,
				HttpServletResponse response, String url)
	throws ServletException, IOException
    {
	RequestDispatcher dispatch = request.getRequestDispatcher(url);
	dispatch.forward(request, response);
    }

    protected void printStackTrace(PrintWriter out, String gripe, Exception ex)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        out.println("<h1>"+ gripe +"</h1>");
        out.println(sw.toString());
    }

    private String getServerName()
	throws UnknownHostException
    {
	// get Hostname of the current Server
	InetAddress ip = InetAddress.getLocalHost();
	return ip.getHostName();
    } 

    String decodeUTF8(byte[] bytes) {
	return new String(bytes, UTF8_CHARSET);
    }

    String encodeUTF8(String string) {
	byte[] utf8bytes = string.getBytes(UTF8_CHARSET);
	return utf8bytes.toString();
    }
}
