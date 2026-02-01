import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;
import java.io.*;

public class AddToCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bookId = request.getParameter("id");
        String quantityStr = request.getParameter("quantity");

        try {
            int quantity = Integer.parseInt(quantityStr); 
            Class.forName("com.mysql.jdbc.Driver");

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_catalog", "root", "root")) {
                
                String query = "INSERT INTO cart (book_id, quantity) VALUES (?, ?) " +
                               "ON DUPLICATE KEY UPDATE quantity = quantity + ?";

                PreparedStatement ps = con.prepareStatement(query);
                ps.setInt(1,Integer.parseInt(bookId));  
                ps.setInt(2,quantity);  
                ps.setInt(3,quantity);  

               ps.executeUpdate();

                response.getWriter().println("Book added to cart successfully.");
                response.sendRedirect("viewcart.html"); 
            }
        } catch (ClassNotFoundException e) {
            response.getWriter().println("JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            response.getWriter().println("SQL Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            response.getWriter().println("Invalid quantity format.");
        }
    }
}
