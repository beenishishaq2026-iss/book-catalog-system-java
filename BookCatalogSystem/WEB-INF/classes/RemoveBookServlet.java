import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class RemoveBookServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bookIdParam = request.getParameter("bookId");

        if (bookIdParam == null || bookIdParam.isEmpty()) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("Error: bookId parameter is missing or empty.");
            return;
        }

        int bookId;
        try {
            bookId = Integer.parseInt(bookIdParam);
        } catch (NumberFormatException e){
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("Error: bookId must be a valid integer.");
            return;
        }

        String url = "jdbc:mysql://localhost:3306/book_catalog";
        String user = "root";
        String password = "root";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);

            String query = "DELETE FROM books WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, bookId);

            int rowsAffected = stmt.executeUpdate();
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            if (rowsAffected > 0) {
                out.println("Book removed successfully!");
            } else {
                out.println("No book found with the given ID.");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("An error occurred while removing the book.");
        }
    }
}
