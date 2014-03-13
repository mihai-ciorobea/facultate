<%@ page import="beans.NameHandler" %>
 <jsp:useBean id="mybean" scope="page" class="beans.NameHandler"/> 
 <jsp:setProperty name="mybean" property="*"/> <html>
<head>
    <title>Hello, Bean User!</title>
</head>
<table border="0" width="700">
    <tr>
        <td width="150"> &nbsp; </td>
        <td width="550">
            <h2>Your name please ?</h2>
        </td>
    </tr>
    <tr>
        <td width="150" &nbsp;
    </td>
    <td width="550">
        <form method="get">
            <input type="text" name="username" size="25">
            <br>
            <input type="submit" value="Submit">
        </form>
    </td>
</tr>
</table>
<% 
	String name= request.getParameter("username"); 
	if ( name==null || name.trim().length()>0 ) { %> 
	<%@ include file="response.jsp" %> 
	<% } else { %>
	 <h2>
	No name, try again!
	</h2> 
	<% } %> 
</body>
</html>