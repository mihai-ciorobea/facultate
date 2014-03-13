import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ParamServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String first, last;
        first = request.getParameter("first");
        last = request.getParameter("last");
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        if (first != null || last != null) {
            out.println("<html>");
            out.println("<body>");
            out.print("<hr><h1>");
            out.print("Hello " + first + " " + last + "!");
            out.println("</h1><hr>");
            out.println("</body>");
            out.println("</html>");
        } else out.println("No Parameters, Please enter some");
    }
}
