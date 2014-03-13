import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class FormServlet extends HttpServlet {
    String first, last;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Request Parameters Example</title>");
        out.println("</head>");
        out.println("<body>");
        out.print("<form method=\"get\">");
        out.println("<p>");
        out.println("First Name:");
        out.println("<input type=text size=20 name=firstname>");
        out.println("<br>");
        out.println("Last Name:");
        out.println("<input type=text size=20 name=lastname>");
        out.println("<br>");
        out.println("<input type=submit value=\"Submit\">");
        out.println("</form>");
        first = request.getParameter("firstname");
        last = request.getParameter("lastname");
        if (first == null && last == null) {
            out.println("No parameters, please enter some!");
        } else {
            if (first.length() == 0 && last.length() == 0) {
                out.println("Empty parameters, please enter values!");
            } else {
                out.print("<h1>");
                out.print("Hello " + first + " " + last + "!");
                out.println("</h1>");
            }
        }
        out.println("</body>");
        out.println("</html>");
    }
}