import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class RemoveFromCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        
        String bookIdParam = request.getParameter("id");

        if (bookIdParam == null || bookIdParam.isEmpty()) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h3>Error: bookId parameter is missing or empty.</h3>");
            out.println("<a href='usercatalog.html'>Back to Catalog</a>");
            out.println("</body></html>");
            return;
        }

        int bookId;
        try {
            bookId = Integer.parseInt(bookIdParam);
        } catch (NumberFormatException e) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h3>Error: bookId must be a valid integer.</h3>");
            out.println("<a href='usercatalog.html'>Back to Catalog</a>");
            out.println("</body></html>");
            return;
        }

        String url = "jdbc:mysql://localhost:3306/book_catalog";
        String user = "root";
        String password = "root";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection(url, user, password);

            String query = "DELETE FROM books WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, bookId);

            int rowsAffected = stmt.executeUpdate();

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            if (rowsAffected > 0) {
                out.println("<h3>Book removed successfully!</h3>");
				out.println("<br><a href='logout.html'>logout</a><br>");
            } else {
                out.println("<h3>No book found with the given ID.</h3>");
            }
            out.println("<a href='usercatalog.html'>Back to Catalog</a>");
            out.println("</body></html>");
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h3>An error occurred while removing the book.</h3>");
            out.println("<a href='usercatalog.html'>Back to Catalog</a>");
            out.println("</body></html>");
        } finally {
     
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
