import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class UpdateBookServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        int bookId = Integer.parseInt(request.getParameter("bookId"));
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        double price = Double.parseDouble(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        String url = "jdbc:mysql://localhost:3306/book_catalog";
        String dbUser = "root";
        String dbPassword = "root";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection(url, dbUser, dbPassword);

            String query = "UPDATE books SET title = ?, author = ?, price = ?, quantity = ? WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setDouble(3, price);
            stmt.setInt(4, quantity);
            stmt.setInt(5, bookId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                out.println("<h3>Book details updated successfully!</h3>");
            } else {
                out.println("<h3>Book not found or no changes made.</h3>");
            }
        } catch (SQLException e) {
            out.println("<h3>Error occurred: " + e.getMessage() + "</h3>");
            e.printStackTrace();
        } catch (Exception e) {
            out.println("<h3>Unexpected error occurred: " + e.getMessage() + "</h3>");
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        out.println("<br><a href='admindashboard.html'>Back to Admin Dashboard</a>");
    }
}
