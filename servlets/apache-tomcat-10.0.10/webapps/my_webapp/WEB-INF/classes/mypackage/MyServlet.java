 package mypackage;
    import java.io.*;
    import jakarta.servlet.*;
    import jakarta.servlet.http.*;
    public class MyServlet extends HttpServlet {
      public void doGet(HttpServletRequest req, HttpServletResponse res)
                        throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println("<html><big>I'm a servlet!!</big></html>");
      }
    }

